package io.automaintenance.api.vehicle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultVehicleService implements VehicleService{

    @Autowired
    VehicleResponseMapper vehicleResponseMapper;

    @Override
    public VehicleResponse addNewVehicle(VehicleRequest vehicleRequest, String garageId, String vehicleId) {

        VehicleResponse vehicleResponse
        = vehicleResponseMapper.map(vehicleRequest, garageId, vehicleId);

        return vehicleResponse;
    }
}
