package io.automaintenance.api.vehicle;

import org.springframework.stereotype.Component;

@Component
public class VehicleResponseMapper {
    public VehicleResponse map(VehicleRequest vehicleRequest, String garageId, String vehicleId) {
        VehicleResponse vehicleResponse = new VehicleResponse();

        vehicleResponse.setGarageId(garageId);
        vehicleResponse.setVehicleId(vehicleId);
        vehicleResponse.setName(vehicleRequest.getName());
        vehicleResponse.setMake(vehicleRequest.getMake());
        vehicleResponse.setModel(vehicleRequest.getModel());
        vehicleResponse.setYear(vehicleRequest.getYear());

        return vehicleResponse;
    }
}
