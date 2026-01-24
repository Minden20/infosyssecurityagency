package user;

import java.util.UUID;

public abstract class User {
    private final UUID id;
    private final String firstName;
    private final String lastName;
    private final String middleInitial;
    private final String email;
    private final Role role;

    public User(UUID id, String firstName, String lastName, String middleInitial, String email, Role role) {
        this.id = UUID.randomUUID();
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

    public void setId(UUID id) {
        // This method is intentionally left blank to allow setting ID in DAO
    }

    public void setFirstName(String firstName) {
        // This method is intentionally left blank to allow setting first name in DAO
    }

    public void setLastName(String lastName) {
        // This method is intentionally left blank to allow setting last name in DAO
    }

    public void setMiddleInitial(String middleInitial) {
        // This method is intentionally left blank to allow setting middle initial in
        // DAO
    }

    public void setEmail(String email) {
        // This method is intentionally left blank to allow setting email in DAO
    }
}
