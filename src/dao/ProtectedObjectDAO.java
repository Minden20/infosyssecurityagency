package dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import model.ProtectedObject;
import util.JsonFileHandler;
import util.SimpleJsonParser;
import util.SimpleJsonParser.JsonObject;

public class ProtectedObjectDAO {
    private static final String FILE_PATH = "data/objects.json";
    private final JsonFileHandler fileHandler;

    public ProtectedObjectDAO() {
        this.fileHandler = new JsonFileHandler();
    }

    public boolean create(ProtectedObject object) throws IOException {
        List<ProtectedObject> objects = findAll();
        objects.add(object);
        return saveAll(objects);
    }

    public Optional<ProtectedObject> findById(UUID id) throws IOException {
        return findAll().stream()
                .filter(o -> o.getId().equals(id))
                .findFirst();
    }

    public List<ProtectedObject> findAll() throws IOException {
        List<ProtectedObject> objects = new ArrayList<>();

        if (!fileHandler.fileExists(FILE_PATH)) {
            return objects;
        }

        try {
            String content = fileHandler.readFile(FILE_PATH);
            if (content.trim().isEmpty()) {
                return objects;
            }
            List<JsonObject> jsonArray = SimpleJsonParser.parseArray(content);

            for (JsonObject jsonObj : jsonArray) {
                UUID id = UUID.fromString((String) jsonObj.get("id"));
                String name = (String) jsonObj.get("name");
                String address = (String) jsonObj.get("address");
                String type = (String) jsonObj.get("type");

                objects.add(new ProtectedObject(id, name, address, type));
            }
        } catch (IOException e) {
            throw new IOException("Error parsing ProtectedObject JSON file", e);
        }

        return objects;
    }

    public boolean update(ProtectedObject object) throws IOException {
        List<ProtectedObject> objects = findAll();
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).getId().equals(object.getId())) {
                objects.set(i, object);
                return saveAll(objects);
            }
        }
        return false;
    }

    public boolean delete(UUID id) throws IOException {
        List<ProtectedObject> objects = findAll();
        boolean removed = objects.removeIf(o -> o.getId().equals(id));
        if (removed) {
            return saveAll(objects);
        }
        return false;
    }

    private boolean saveAll(List<ProtectedObject> objects) throws IOException {
        List<JsonObject> jsonArray = new ArrayList<>();

        for (ProtectedObject obj : objects) {
            JsonObject jsonObj = new JsonObject();
            jsonObj.put("id", obj.getId().toString());
            jsonObj.put("name", obj.getName());
            jsonObj.put("address", obj.getAddress());
            jsonObj.put("type", obj.getType());
            jsonArray.add(jsonObj);
        }

        fileHandler.writeFile(FILE_PATH, SimpleJsonParser.arrayToJsonString(jsonArray));
        return true;
    }
}
