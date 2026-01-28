package util;

import exception.ValidationException;
import java.util.regex.Pattern;
import user.User;

public class Validator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    public static void validateEmail(String email) throws ValidationException {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Поле email не может быть пустым.");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Некоректений формат email.");
        }
    }

    public static void validatePassword(String password) throws ValidationException {
        if (password == null || password.trim().isEmpty()) {
            throw new ValidationException("Пароль не может быть пустым.");
        }
        if (password.length() < 8) {
            throw new ValidationException("Пароль повинен містити мінімум 8 символів.");
        }
    }

    public static void validateUser(User user) throws ValidationException {
        if (user == null) {
            throw new ValidationException("Користувач не може бути порожнім.");
        }
        validateEmail(user.getEmail());
        validatePassword(user.getPassword());
        
        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            throw new ValidationException("Ім'я не може бути порожнім.");
        }
        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            throw new ValidationException("Прізвище не може бути порожнім.");
        }
    }
}
