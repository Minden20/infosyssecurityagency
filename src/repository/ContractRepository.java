package repository;

import java.util.UUID;
import model.Contract;

public interface ContractRepository extends CrudRepository<Contract, UUID> {
}
