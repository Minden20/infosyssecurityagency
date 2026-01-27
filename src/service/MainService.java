package service;

import dao.ContractDAO;
import dao.ProtectedObjectDAO;
import dao.UserDAO;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import model.Contract;
import model.Contract.ContractStatus;
import model.ProtectedObject;
import user.User;

public class MainService {
    private final UserDAO userDAO;
    private final ContractDAO contractDAO;
    private final ProtectedObjectDAO protectedObjectDAO;

    public MainService() {
        this.userDAO = new UserDAO();
        this.contractDAO = new ContractDAO();
        this.protectedObjectDAO = new ProtectedObjectDAO();
    }

    // --- User Management (Admin) ---

    public List<User> getAllUsers() throws IOException {
        return userDAO.findAll();
    }

    public void createUser(String firstName, String lastName, String middleInitial, String email, String password, User.Role role)
            throws Exception {
        new service.AuthService().createUser(firstName, lastName, middleInitial, email, password, role);
    }

    public boolean deleteUser(String email) throws IOException {
        User user = userDAO.findByEmail(email);
        if (user != null) {
            return userDAO.delete(user.getId());
        }
        return false;
    }

    // --- Report Management (Guard) ---
    private final dao.ReportDAO reportDAO = new dao.ReportDAO();

    public void createReport(User guard, String content) throws IOException {
        model.Report report = new model.Report(null, guard.getId(), content, null);
        reportDAO.create(report);
    }

    public List<model.Report> getGuardReports(User guard) throws IOException {
        // In real app, filter by guard ID if needed, or return all for now per requirements (or shift reports)
        // Let's filter by author for "My Reports" context
        return reportDAO.findAll().stream()
                .filter(r -> r.getAuthorId().equals(guard.getId()))
                .collect(Collectors.toList());
    }

    // --- Protected Object Management ---

    public List<ProtectedObject> getAllProtectedObjects() throws IOException {
        return protectedObjectDAO.findAll();
    }

    public ProtectedObject createProtectedObject(String name, String address, String type) throws IOException {
        ProtectedObject object = new ProtectedObject(null, name, address, type);
        protectedObjectDAO.create(object);
        return object;
    }
    
    // --- Contract Management (Client) ---

    public List<Contract> getClientContracts(User client) throws IOException {
        return contractDAO.findAll().stream()
                .filter(c -> c.getClientId() != null && c.getClientId().equals(client.getId()))
                .collect(Collectors.toList());
    }
    
    public void requestContract(User client, ProtectedObject object) throws IOException {
        // Create a pending contract
        Contract contract = Contract.builder()
                .clientId(client.getId())
                .protectedObject(object)
                .status(ContractStatus.PENDING)
                .startDate(LocalDate.now())
                .build();
        contractDAO.create(contract);
    }
}
