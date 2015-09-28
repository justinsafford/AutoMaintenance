package io.automaintenance.api.repos;

import io.automaintenance.api.maintenance.MaintenanceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MaintenanceRepository extends MongoRepository<MaintenanceEntity, String>{
}
