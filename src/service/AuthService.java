package service;

import dao.UserDAO;
import java.io.IOException;
import java.util.UUID;
import user.Client;
import user.User;

public class AuthService {
    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public User registerUser(String firstName, String lastName, String middleInitial, String email, String password) {
        try {
            if (userDAO.findByEmail(email) != null) {
                System.out.println("User with this email already exists.");
                return null;
            }

            String hashedPassword = util.PasswordUtil.hashPassword(password);
            User user = new Client(UUID.randomUUID(), hashedPassword, firstName, lastName, middleInitial, email);
            if (userDAO.create(user)) {
                return user;
            }
        } catch (IOException e) {
        }
        return null;
    }
}
