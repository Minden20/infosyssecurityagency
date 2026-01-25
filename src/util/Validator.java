package util;

import exception.ValidationException;
import user.User;
import java.util.regex.Pattern;

public class Validator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    public static void validateEmail(String email) throws ValidationException {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email cannot be empty.");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Invalid email format.");
        }
    }

    public static void validatePassword(String password) throws ValidationException {
        if (password == null || password.trim().isEmpty()) {
            throw new ValidationException("Password cannot be empty.");
        }
        if (password.length() < 8) {
            throw new ValidationException("Password must be at least 8 characters long.");
        }
    }

    public static void validateUser(User user) throws ValidationException {
        if (user == null) {
            throw new ValidationException("User object cannot be null.");
        }
        validateEmail(user.getEmail());
        validatePassword(user.getPassword());
        
        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            throw new ValidationException("First name cannot be empty.");
        }
        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            throw new ValidationException("Last name cannot be empty.");
        }
    }
}
