package ui;

import exception.AuthException;
import exception.UserAlreadyExistsException;
import exception.ValidationException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import model.Contract;
import model.ProtectedObject;
import service.AuthService;
import service.MainService;
import user.User;

public class MainUI {
    private final AuthService authService;
    private final Scanner scanner;
    private User currentUser;

    public MainUI() {
        this.authService = new AuthService();
        this.mainService = new MainService(); // Initialize MainService
        this.scanner = new Scanner(System.in);
        this.currentUser = null;
    }
    
    private final MainService mainService;

    // ... (existing code usually fits, but I'll replace the full relevant blocks)

    public void start() {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  Інформаційна система охоронного агенції ║");
        System.out.println("╚══════════════════════════════════════════╝\n");

        while (true) {
            if (currentUser == null) {
                showAuthMenu();
            } else {
                showMainMenu();
            }
        }
    }

    private void showAuthMenu() {
        System.out.println("\n=== Меню авторизації ===");
        System.out.println("1. Вхід");
        System.out.println("2. Реєстрація");
        System.out.println("3. Вихід");
        System.out.print("\nОберіть дію: ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> handleLogin();
            case "2" -> handleRegister();
            case "3" -> {
                System.out.println("\nДо побачення!");
                System.exit(0);
            }
            default -> System.out.println("\n Не існує такої функції.");
        }
    }

    private void handleLogin() {
        System.out.println("\n--- Вхід ---");
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Пароль: ");
        String password = scanner.nextLine();

        try {
            currentUser = authService.login(email, password);
            System.out.println("\n Ви успішно увійшли! Ласкаво просимо, " + currentUser.getFirstName() + "!");
        } catch (AuthException | ValidationException | IOException e) {
            System.out.println("\n Помилка входу: " + e.getMessage());
        }
    }

    private void handleRegister() {
        System.out.println("\n--- Реєстрація ---");
        System.out.print("Ім'я: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Прізвище: ");
        String lastName = scanner.nextLine().trim();
        System.out.print("По батькові (або пропустіть): ");
        String middleInitial = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Пароль (мінімум 6 символів): ");
        String password = scanner.nextLine();

        try {
            currentUser = authService.registerUser(firstName, lastName, middleInitial, email, password);
            System.out.println("\n✓ Реєстрація успішна! Ви увійшли як клієнт.");
        } catch (ValidationException | UserAlreadyExistsException | IOException e) {
            System.out.println("\n Помилка реєстрації: " + e.getMessage());
        }
    }

    private void showMainMenu() {
        System.out.println("\n=== Головне меню ===");
        System.out.println("Користувач: " + currentUser.getFirstName() + " " + currentUser.getLastName() + 
                          " (Роль: " + currentUser.getRole() + ")");
        System.out.println("1. Переглянути профіль");
        System.out.println("2. Змінити пароль");
        
        if (null == currentUser.getRole()) {
            System.out.println("3. [КЛІЄНТ] Переглянути мої контракти");
        } else switch (currentUser.getRole()) {
            case ADMIN -> System.out.println("3. [АДМІН] Управління користувачами");
            case GUARD -> System.out.println("3. [ОХОРОНЕЦЬ] Переглянути захищені об'єкти");
            default -> System.out.println("3. [КЛІЄНТ] Переглянути мої контракти");
        }
        
        System.out.println("4. Вихід з облікового запису");
        System.out.print("\nОберіть дію: ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> showProfile();
            case "2" -> changePassword();
            case "3" -> handleRoleSpecificMenu();
            case "4" -> logout();
            default -> System.out.println("\n Невірний вибір. Спробуйте ще раз.");
        }
    }

    private void showProfile() {
        System.out.println("\n--- Мій профіль ---");
        System.out.println("ID: " + currentUser.getId());
        System.out.println("Ім'я: " + currentUser.getFirstName());
        System.out.println("Прізвище: " + currentUser.getLastName());
        System.out.println("По батькові: " + (currentUser.getMiddleInitial().isEmpty() ? "не вказано" : currentUser.getMiddleInitial()));
        System.out.println("Email: " + currentUser.getEmail());
        System.out.println("Роль: " + currentUser.getRole());
    }

    private void changePassword() {
        System.out.println("\n--- Зміна пароля ---");
        System.out.print("Поточний пароль: ");
        String currentPassword = scanner.nextLine();
        System.out.print("Новий пароль: ");
        String newPassword = scanner.nextLine();
        System.out.print("Підтвердіть новий пароль: ");
        String confirmPassword = scanner.nextLine();

        if (!newPassword.equals(confirmPassword)) {
            System.out.println("\n Паролі не співпадають!");
        } else if (newPassword.length() < 6) {
            System.out.println("\n Пароль повинен містити мінімум 6 символів!");
        } else {
            System.out.println("\n Пароль успішно змінений!");
        }
    }

    private void handleRoleSpecificMenu() {
        switch (currentUser.getRole()) {
            case ADMIN -> showAdminMenu();
            case GUARD -> showGuardMenu();
            case CLIENT -> showClientMenu();
        }
    }

    private void logout() {
        System.out.println("\n Ви вийшли з облікового запису.");
        currentUser = null;
    }

    private void showAdminMenu() {
        while (true) {
            System.out.println("\n=== Панель адміністратора ===");
            System.out.println("1. Переглянути всіх користувачів");
            System.out.println("2. Створити користувача");
            System.out.println("3. Видалити користувача");
            System.out.println("4. Назад");
            System.out.print("\nОберіть дію: ");
    
            String choice = scanner.nextLine().trim();
    
            try {
                switch (choice) {
                    case "1" -> {
                        System.out.println("\n--- Список користувачів ---");
                        List<User> users = mainService.getAllUsers();
                        users.forEach(u -> System.out.println(u.getRole() + ": " + u.getFirstName() + " " + u.getLastName() + " (" + u.getEmail() + ")"));
                    }
                    case "2" -> {
                        System.out.println("\n--- Створення користувача ---");
                        System.out.print("Ім'я: ");
                        String firstName = scanner.nextLine().trim();
                        System.out.print("Прізвище: ");
                        String lastName = scanner.nextLine().trim();
                        System.out.print("По батькові: ");
                        String middleInitial = scanner.nextLine().trim();
                        System.out.print("Email: ");
                        String email = scanner.nextLine().trim();
                        System.out.print("Пароль: ");
                        String password = scanner.nextLine();
                        
                        System.out.println("Оберіть роль:");
                        System.out.println("1. ADMIN");
                        System.out.println("2. GUARD");
                        System.out.println("3. CLIENT");
                        String roleChoice = scanner.nextLine().trim();
                        User.Role role = switch(roleChoice) {
                            case "1" -> User.Role.ADMIN;
                            case "2" -> User.Role.GUARD;
                            default -> User.Role.CLIENT;
                        };

                        try {
                            mainService.createUser(firstName, lastName, middleInitial, email, password, role);
                            System.out.println("Користувача створено успішно!");
                        } catch (Exception e) {
                            System.out.println("Помилка створення: " + e.getMessage());
                        }
                    }
                    case "3" -> {
                        System.out.println("\n--- Видалення користувача ---");
                        System.out.print("Введіть email користувача: ");
                        String email = scanner.nextLine().trim();
                        if (mainService.deleteUser(email)) {
                            System.out.println("Користувача видалено.");
                        } else {
                            System.out.println("Користувача не знайдено.");
                        }
                    }
                    case "4" -> { return; }
                    default -> System.out.println("\n Невірний вибір.");
                }
            } catch (IOException e) {
                System.out.println("Помилка ввода/виводу: " + e.getMessage());
            }
        }
    }

    private void showGuardMenu() {
        while (true) {
            System.out.println("\n=== Панель охоронця ===");
            System.out.println("1. Переглянути захищені об'єкти");
            System.out.println("2. Звіти (Створити/Переглянути)");
            System.out.println("3. Назад");
            System.out.print("\nОберіть дію: ");
    
            String choice = scanner.nextLine().trim();
    
            try {
                switch (choice) {
                    case "1" -> {
                        System.out.println("\n--- Захищені об'єкти ---");
                        List<ProtectedObject> objects = mainService.getAllProtectedObjects();
                        if (objects.isEmpty()) {
                            System.out.println("Немає зареєстрованих об'єктів.");
                        } else {
                            objects.forEach(System.out::println);
                        }
                    }
                    case "2" -> {
                        System.out.println("\n--- Звіти ---");
                        System.out.println("1. Створити звіт");
                        System.out.println("2. Мої звіти");
                        String subChoice = scanner.nextLine().trim();
                        if ("1".equals(subChoice)) {
                            System.out.print("Введіть текст звіту: ");
                            String content = scanner.nextLine();
                            mainService.createReport(currentUser, content);
                            System.out.println("Звіт збережено.");
                        } else if ("2".equals(subChoice)) {
                            List<model.Report> reports = mainService.getGuardReports(currentUser);
                            if (reports.isEmpty()) System.out.println("Звітів немає.");
                            else reports.forEach(System.out::println);
                        }
                    }
                    case "3" -> { return; }
                    default -> System.out.println("\n Невірний вибір.");
                }
            } catch (IOException e) {
                System.out.println("Помилка: " + e.getMessage());
            }
        }
    }

    private void showClientMenu() {
         while (true) {
            System.out.println("\n=== Панель клієнта ===");
            System.out.println("1. Переглянути мої контракти");
            System.out.println("2. Створити запит на охорону (Створити об'єкт та контракт)");
            System.out.println("3. Назад");
            System.out.print("\nОберіть дію: ");
    
            String choice = scanner.nextLine().trim();
    
            try {
                switch (choice) {
                    case "1" -> {
                        System.out.println("\n--- Мої контракти ---");
                        List<Contract> contracts = mainService.getClientContracts(currentUser);
                        if (contracts.isEmpty()) {
                            System.out.println("У вас немає активних контрактів.");
                        } else {
                            for (Contract c : contracts) {
                                System.out.println("Контракт ID: " + c.getId());
                                System.out.println("  Об'єкт: " + c.getProtectedObject());
                                System.out.println("  Статус: " + c.getStatus());
                                System.out.println("  Ціна: " + c.getPrice());
                                System.out.println("-------------------------");
                            }
                        }
                    }
                    case "2" -> {
                        System.out.println("\n--- Нова заявка ---");
                        System.out.print("Назва об'єкту: ");
                        String name = scanner.nextLine().trim();
                        System.out.print("Адреса: ");
                        String address = scanner.nextLine().trim();
                        System.out.print("Тип об'єкту (Квартира/Офіс/Склад): ");
                        String type = scanner.nextLine().trim();
                        
                        // Create object and link to contract
                        ProtectedObject obj = mainService.createProtectedObject(name, address, type);
                        mainService.requestContract(currentUser, obj);
                        System.out.println("Заявку створено!");
                    }
                    case "3" -> { return; }
                    default -> System.out.println("\n Невірний вибір.");
                }
            } catch (IOException e) {
                System.out.println("Помилка: " + e.getMessage());
            }
        }
    }
}
