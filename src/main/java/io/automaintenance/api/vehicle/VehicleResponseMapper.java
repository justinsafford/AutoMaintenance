package io.automaintenance.api.vehicle;

import org.springframework.stereotype.Component;

@Component
public class VehicleResponseMapper {
    public VehicleEntity map(VehicleRequest vehicleRequest, String garageId, String vehicleId) {
        VehicleEntity vehicleEntity = new VehicleEntity();

        vehicleEntity.setGarageId(garageId);
        vehicleEntity.setVehicleId(vehicleId);
        vehicleEntity.setName(vehicleRequest.getName());
        vehicleEntity.setMake(vehicleRequest.getMake());
        vehicleEntity.setModel(vehicleRequest.getModel());
        vehicleEntity.setYear(vehicleRequest.getYear());

        return vehicleEntity;
    }
}
