package service;

import dao.UserDAO;
import exception.AuthException;
import exception.UserAlreadyExistsException;
import exception.ValidationException;

import java.io.IOException;
import java.util.UUID;
import user.Admin;
import user.Client;
import user.Guard;
import user.User;
import util.PasswordUtil;
import util.Validator;

public class AuthService {
    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public User registerUser(String firstName, String lastName, String middleInitial, String email, String password) 
            throws ValidationException, UserAlreadyExistsException, IOException {
        return createUser(firstName, lastName, middleInitial, email, password, User.Role.CLIENT);
    }

    public User createUser(String firstName, String lastName, String middleInitial, String email, String password, User.Role role)
            throws ValidationException, UserAlreadyExistsException, IOException {
            
        // Validate inputs
        Validator.validateEmail(email);
        Validator.validatePassword(password);
        if (firstName == null || firstName.trim().isEmpty()) throw new ValidationException("First name is required.");
        if (lastName == null || lastName.trim().isEmpty()) throw new ValidationException("Last name is required.");

        // Check if user exists
        if (userDAO.findByEmail(email) != null) {
            throw new UserAlreadyExistsException("User with this email already exists.");
        }

        String hashedPassword = PasswordUtil.hashPassword(password);
        
        User user;
        switch (role) {
            case CLIENT -> user = new Client(UUID.randomUUID(), hashedPassword, firstName, lastName, middleInitial, email);
            case ADMIN -> user = new Admin(UUID.randomUUID(), hashedPassword, firstName, lastName, middleInitial, email);
            case GUARD -> user = new Guard(UUID.randomUUID(), hashedPassword, firstName, lastName, middleInitial, email);
            default -> throw new ValidationException("Invalid role");
        }
        
        userDAO.create(user);
        return user;
    }

    public User login(String email, String password) throws AuthException, ValidationException, IOException {
        Validator.validateEmail(email);
        if (password == null || password.isEmpty()) {
            throw new ValidationException("Password is required.");
        }

        User user = userDAO.findByEmail(email);
        if (user == null) {
            throw new AuthException("Invalid email or password.");
        }

        if (!PasswordUtil.checkPassword(password, user.getPassword())) {
            throw new AuthException("Invalid email or password.");
        }

        return user;
    }
}
