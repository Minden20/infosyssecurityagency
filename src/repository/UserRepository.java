package repository;

import java.io.IOException;
import java.util.UUID;
import user.User;

public interface UserRepository extends CrudRepository<User, UUID> {
    User findByEmail(String email) throws IOException;
}
