package autoMaintProgram.repos;

import autoMaintProgram.maintenance.MaintenanceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MaintenanceRepository extends MongoRepository<MaintenanceEntity, String>{
}
