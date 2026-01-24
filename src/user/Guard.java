package user;

import java.util.UUID;

public class Guard extends User {
    public Guard() {
        super(UUID.randomUUID(), "", "", "", "", Role.GUARD);
    }

    public Guard(UUID id, String firstName, String lastName, String middleInitial, String email) {
        super(id, firstName, lastName, middleInitial, email, Role.GUARD);
    }
}
