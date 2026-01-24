package user;

import java.util.UUID;

public class Admin extends User {
    public Admin() {
        super(UUID.randomUUID(), "", "", "", "", Role.ADMIN);
    }

    public Admin(UUID id, String firstName, String lastName, String middleInitial, String email) {
        super(id, firstName, lastName, middleInitial, email, Role.ADMIN);
    }
}
