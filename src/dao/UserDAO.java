package dao;

import exception.UserAlreadyExistsException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import user.Admin;
import user.Client;
import user.Guard;
import user.User;
import util.JsonFileHandler;
import util.SimpleJsonParser;
import util.SimpleJsonParser.JsonObject;

public class UserDAO {
    /**
     * Шлях до JSON файлу з даними користувачів.
     */
    /**
     * Шлях до JSON файлу з даними користувачів.
     */
    private static final String FILE_PATH = "data/users.json";

    /**
     * Об'єкт для роботи з JSON файлами.
     */
    private final JsonFileHandler fileHandler;

    /**
     * Конструктор.
     * Ініціалізує об'єкт для роботи з файлами.
     */
    public UserDAO() {
        this.fileHandler = new JsonFileHandler();
    }

    /**
     * Створює нового користувача та зберігає його у файл.
     * 
     * @param user користувач для створення
     * @return true, якщо користувач успішно створений, false - інакше
     * @throws IOException якщо виникла помилка при роботі з файлом
     * @throws UserAlreadyExistsException 
     */
    public boolean create(User user) throws IOException, UserAlreadyExistsException {
        List<User> users = findAll();
        if (users.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(user.getEmail()))) {
            throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists.");
        }
        users.add(user);
        return saveAll(users);
    }

    /**
     * Знаходить користувача за email адресою.
     * 
     * @param email email адреса користувача
     * @return користувач з вказаним email або null, якщо не знайдено
     * @throws IOException якщо виникла помилка при роботі з файлом
     */
    public User findByEmail(String email) throws IOException {
        List<User> users = findAll();
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Отримує всіх користувачів з файлу.
     * 
     * @return список всіх користувачів
     * @throws IOException якщо виникла помилка при роботі з файлом
     */
    public List<User> findAll() throws IOException {
        List<User> users = new ArrayList<>();

        if (!fileHandler.fileExists(FILE_PATH)) {
            return users;
        }

        try {
            String content = fileHandler.readFile(FILE_PATH);
            if (content.trim().isEmpty()) {
                return users;
            }
            List<JsonObject> jsonArray = SimpleJsonParser.parseArray(content);

            for (JsonObject jsonUser : jsonArray) {
                String roleStr = (String) jsonUser.get("role");
                User.Role role = User.Role.valueOf(roleStr);

                UUID id; 
                Object idValue = jsonUser.get("id");
                if (idValue == null) {
                    id = null; 
                } else if (idValue instanceof UUID uuid) {
                    id = uuid;
                } else {
                    try {
                        id = UUID.fromString(idValue.toString());
                    } catch (IllegalArgumentException e) {
                        id = null; 
                    }
                }
                
                String firstName = (String) jsonUser.get("firstName");
                String lastName = (String) jsonUser.get("lastName");
                String middleInitial = (String) jsonUser.get("middleInitial");
                String email = (String) jsonUser.get("email");
                String password = (String) jsonUser.get("password");

                User user;
                switch (role) {
                    case CLIENT -> user = new Client(id, password, firstName, lastName, middleInitial, email);
                    case ADMIN -> user = new Admin(id, password, firstName, lastName, middleInitial, email);
                    case GUARD -> user = new Guard(id, password, firstName, lastName, middleInitial, email);
                    default -> throw new IOException("Невідома роль користувача: " + role);
                }
                users.add(user);
            }
        } catch (IOException e) {
            throw new IOException("Помилка парсингу JSON файлу", e);
        }

        return users;
    }

    /**
     * Оновлює дані користувача у файлі.
     * 
     * @param user користувач з оновленими даними
     * @return true, якщо користувач успішно оновлений, false - інакше
     * @throws IOException якщо виникла помилка при роботі з файлом
     */
    public boolean update(User user) throws IOException {
        List<User> users = findAll();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                users.set(i, user);
                return saveAll(users);
            }
        }
        return false;
            }

    /**
     * Видаляє користувача за ідентифікатором.
     * 
     * @param id ідентифікатор користувача для видалення
     * @return true, якщо користувач успішно видалений, false - інакше
     * @throws IOException якщо виникла помилка при роботі з файлом
     */
    public boolean delete(UUID id) throws IOException {
        List<User> users = findAll();
        boolean removed = users.removeIf(user -> user.getId().equals(id));
        if (removed) {
            return saveAll(users);
    }
        return false;
        }

    /**
     * Зберігає список користувачів у JSON файл.
     * 
     * @param users список користувачів для збереження
     * @return true, якщо дані успішно збережені
     * @throws IOException якщо виникла помилка при записі файлу
     */
    private boolean saveAll(List<User> users) throws IOException {
        List<JsonObject> jsonArray = new ArrayList<>();

        for (User user : users) {
            JsonObject jsonUser = new JsonObject();
            jsonUser.put("id", user.getId());
            jsonUser.put("firstName", user.getFirstName());
            jsonUser.put("lastName", user.getLastName());
            jsonUser.put("middleInitial", user.getMiddleInitial());
            jsonUser.put("email", user.getEmail());
            jsonUser.put("password", user.getPassword());
            jsonUser.put("role", user.getRole().toString());
            jsonArray.add(jsonUser);
        }

        fileHandler.writeFile(FILE_PATH, SimpleJsonParser.arrayToJsonString(jsonArray));
        return true;
    }
}
