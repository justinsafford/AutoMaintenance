package autoMaintProgram;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface VehicleRepository extends MongoRepository<VehicleEntity, String>{
}
