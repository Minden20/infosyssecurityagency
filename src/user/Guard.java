package user;

import java.util.UUID;

public class Guard extends User {
    public Guard() {
        super(UUID.randomUUID(), "", "", "", "", "", Role.GUARD);
    }

    public Guard(UUID id, String password, String firstName, String lastName, String middleInitial, String email) {
        super(id, password, firstName, lastName, middleInitial, email, Role.GUARD);
    }
}
