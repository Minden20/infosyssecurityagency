package repository;

import java.util.UUID;
import model.ProtectedObject;

public interface ProtectedObjectRepository extends CrudRepository<ProtectedObject, UUID> {
}
