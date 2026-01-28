package service;

import exception.AuthException;
import exception.UserAlreadyExistsException;
import exception.ValidationException;
import java.io.IOException;
import user.User;

public interface AuthenticationService {
    User registerUser(String firstName, String lastName, String middleInitial, String email, String password) 
            throws ValidationException, UserAlreadyExistsException, IOException;

    User createUser(String firstName, String lastName, String middleInitial, String email, String password, User.Role role)
            throws ValidationException, UserAlreadyExistsException, IOException;

    User login(String email, String password) throws AuthException, ValidationException, IOException;
}
