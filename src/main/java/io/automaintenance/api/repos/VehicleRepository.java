package io.automaintenance.api.repos;

import io.automaintenance.api.vehicle.VehicleResponse;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VehicleRepository extends MongoRepository<VehicleResponse, String>{
    VehicleResponse findFirstByGarageIdAndVehicleId(String garageId, String vehicleId);

    List<VehicleResponse> findAllByGarageId(String garageId);
}
