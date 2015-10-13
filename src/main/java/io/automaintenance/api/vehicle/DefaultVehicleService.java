package io.automaintenance.api.vehicle;

import io.automaintenance.api.ResourcesNotFoundException;
import io.automaintenance.api.repos.GarageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DefaultVehicleService implements VehicleService{

    @Autowired
    GarageRepository garageRepository;

    @Autowired
    VehicleResponseMapper vehicleResponseMapper;

    @Override
    public VehicleResponse addNewVehicle(VehicleRequest vehicleRequest, String garageId) {

        String vehicleId = UUID.randomUUID().toString();

        if (garageRepository.findOne(garageId) == null) {
            throw new ResourcesNotFoundException("Garage not found");
        }

        VehicleResponse vehicleResponse
            = vehicleResponseMapper.map(vehicleRequest, garageId, vehicleId);

        return vehicleResponse;
    }
}
