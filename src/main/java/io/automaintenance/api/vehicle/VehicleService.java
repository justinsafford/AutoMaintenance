package io.automaintenance.api.vehicle;

import java.util.List;

public interface VehicleService {
    VehicleResponse addVehicle(VehicleRequest vehicleRequest, String garageId);

    VehicleResponse findVehicle(String garageId, String vehicleId);

    List<VehicleResponse> findAllVehicles(String garageId);

    VehicleResponse editVehicle(VehicleRequest vehicleRequest, String garageId, String vehicleId);

    void deleteVehicle(String garageId, String vehicleId);
}
