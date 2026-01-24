package user;

import java.util.UUID;

public abstract class User {
    private final UUID id;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final String middleInitial;
    private final String email;
    private final Role role;

    public User(UUID id, String password, String firstName, String lastName, String middleInitial, String email,
            Role role) {
        this.id = UUID.randomUUID();
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleInitial = middleInitial;
        this.email = email;
        this.role = role;
    }

    public enum Role {
        ADMIN,
        CLIENT,
        GUARD
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }
}
