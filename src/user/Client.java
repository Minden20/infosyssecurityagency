package user;

import java.util.UUID;

public class Client extends User {
    public Client() {
        super(UUID.randomUUID(), "", "", "", "", Role.CLIENT);
    }
    public Client(UUID id, String firstName, String lastName, String middleInitial, String email) {
        super(id, firstName, lastName, middleInitial, email, Role.CLIENT);
    }
}
