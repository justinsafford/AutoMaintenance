package io.automaintenance.api.vehicle;

public interface VehicleService {
    VehicleResponse addNewVehicle(VehicleRequest vehicleRequest, String garageId, String vehicleId);
}
