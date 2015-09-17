package autoMaintProgram.repos;

import autoMaintProgram.vehicle.VehicleEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VehicleRepository extends MongoRepository<VehicleEntity, String>{
    VehicleEntity findFirstByGarageIdAndVehicleId(String garageId, String vehicleId);

    List<VehicleEntity> findAllByGarageId(String garageId);
}