package dao;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import model.Contract;
import model.Contract.ContractStatus;
import model.ProtectedObject;
import user.Guard;
import util.JsonFileHandler;
import util.SimpleJsonParser;
import util.SimpleJsonParser.JsonObject;

public class ContractDAO {
    private static final String FILE_PATH = "data/contracts.json";
    private final JsonFileHandler fileHandler;
    private final UserDAO userDAO;
    private final ProtectedObjectDAO protectedObjectDAO;

    public ContractDAO() {
        this.fileHandler = new JsonFileHandler();
        this.userDAO = new UserDAO();
        this.protectedObjectDAO = new ProtectedObjectDAO();
    }

    public boolean create(Contract contract) throws IOException {
        List<Contract> contracts = findAll();
        contracts.add(contract);
        return saveAll(contracts);
    }

    public List<Contract> findAll() throws IOException {
        List<Contract> contracts = new ArrayList<>();

        if (!fileHandler.fileExists(FILE_PATH)) {
            return contracts;
        }

        try {
            String content = fileHandler.readFile(FILE_PATH);
            if (content.trim().isEmpty()) {
                return contracts;
            }
            List<JsonObject> jsonArray = SimpleJsonParser.parseArray(content);

            for (JsonObject jsonObj : jsonArray) {
                UUID id = UUID.fromString((String) jsonObj.get("id"));
                
                String clientIdStr = (String) jsonObj.get("clientId");
                UUID clientId = clientIdStr != null ? UUID.fromString(clientIdStr) : null;
                
                UUID protectedObjectId = UUID.fromString((String) jsonObj.get("protectedObjectId"));
                Optional<ProtectedObject> protectedObjectOpt = protectedObjectDAO.findById(protectedObjectId);
                
                String guardIdsStr = (String) jsonObj.get("guardIds");
                List<Guard> guards = new ArrayList<>();
                if (guardIdsStr != null && !guardIdsStr.isEmpty()) {
                    String[] ids = guardIdsStr.split(";");
                    for (String guardIdStr : ids) {
                        if (guardIdStr.isEmpty()) continue;
                         userDAO.findAll().stream()
                             .filter(u -> u.getId().toString().equals(guardIdStr) && u instanceof Guard)
                             .map(u -> (Guard) u)
                             .findFirst()
                             .ifPresent(guards::add);
                    }
                }

                String startDateStr = (String) jsonObj.get("startDate");
                String endDateStr = (String) jsonObj.get("endDate");
                LocalDate startDate = startDateStr != null ? LocalDate.parse(startDateStr) : null;
                LocalDate endDate = endDateStr != null ? LocalDate.parse(endDateStr) : null;
                
                double price = Double.parseDouble(jsonObj.get("price").toString());
                ContractStatus status = ContractStatus.valueOf((String) jsonObj.get("status"));

                if (protectedObjectOpt.isPresent()) {
                    contracts.add(new Contract(id, clientId, protectedObjectOpt.get(), guards, startDate, endDate, price, status));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Warning: Error parsing Contract JSON: " + e.getMessage());
        }

        return contracts;
    }

    private boolean saveAll(List<Contract> contracts) throws IOException {
        List<JsonObject> jsonArray = new ArrayList<>();

        for (Contract contract : contracts) {
            JsonObject jsonObj = new JsonObject();
            jsonObj.put("id", contract.getId().toString());
            jsonObj.put("clientId", contract.getClientId() != null ? contract.getClientId().toString() : null);
            jsonObj.put("protectedObjectId", contract.getProtectedObject().getId().toString());
            
            // Serialize guards as semicolon separated string
            String guardIds = contract.getGuards().stream()
                    .map(g -> g.getId().toString())
                    .collect(Collectors.joining(";"));
            jsonObj.put("guardIds", guardIds);

            jsonObj.put("startDate", contract.getStartDate() != null ? contract.getStartDate().toString() : null);
            jsonObj.put("endDate", contract.getEndDate() != null ? contract.getEndDate().toString() : null);
            jsonObj.put("price", contract.getPrice());
            jsonObj.put("status", contract.getStatus().toString());
            
            jsonArray.add(jsonObj);
        }

        fileHandler.writeFile(FILE_PATH, SimpleJsonParser.arrayToJsonString(jsonArray));
        return true;
    }
}
