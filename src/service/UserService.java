package service;

import dao.UserDAO;
import exception.UserAlreadyExistsException;
import exception.UserNotFoundException;
import exception.ValidationException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import user.User;
import util.Validator;

public class UserService {
    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public List<User> getAllUsers() throws IOException {
        return userDAO.findAll();
    }

    public void createUser(User user) throws IOException, ValidationException, UserAlreadyExistsException {
        Validator.validateUser(user);
        userDAO.create(user);
    }

    public void updateUser(User user) throws IOException, ValidationException, UserNotFoundException {
        Validator.validateUser(user);
        userDAO.update(user);
    }

    public void deleteUser(UUID id) throws IOException, UserNotFoundException {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        userDAO.delete(id);
    }
}
