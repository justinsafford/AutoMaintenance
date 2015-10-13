package io.automaintenance.api.vehicle;

public interface VehicleService {
    VehicleResponse addVehicle(VehicleRequest vehicleRequest, String garageId);
}
