package repository;

import java.util.UUID;
import model.Report;

public interface ReportRepository extends CrudRepository<Report, UUID> {
}
