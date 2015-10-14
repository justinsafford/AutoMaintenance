package io.automaintenance.api.vehicle;

import io.automaintenance.api.ResourcesNotFoundException;
import io.automaintenance.api.repos.GarageRepository;
import io.automaintenance.api.repos.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DefaultVehicleService implements VehicleService{

    @Autowired
    GarageRepository garageRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    VehicleResponseMapper vehicleResponseMapper;

    @Override
    public VehicleResponse addVehicle(VehicleRequest vehicleRequest, String garageId) {
        if (garageRepository.findOne(garageId) == null) {
            throw new ResourcesNotFoundException("Garage not found");
        }

        String vehicleId = UUID.randomUUID().toString();
        VehicleResponse vehicleResponse
            = vehicleResponseMapper.map(vehicleRequest, garageId, vehicleId);

        vehicleRepository.save(vehicleResponse);
        return vehicleResponse;
    }

    @Override
    public VehicleResponse findVehicle(String garageId, String vehicleId) {
        VehicleResponse vehicleResponse
                = vehicleRepository.findFirstByGarageIdAndVehicleId(garageId, vehicleId);

        if (vehicleResponse == null) {
            throw new ResourcesNotFoundException("Vehicle not found");
        }

        return vehicleResponse;
    }

    @Override
    public List<VehicleResponse> findAllVehicles(String garageId) {
        List<VehicleResponse> vehicleResponseList = vehicleRepository.findAllByGarageId(garageId);

        //TODO:Check this logic.. Should check for vehicle existence first
        if (vehicleResponseList == null) {
            throw new ResourcesNotFoundException("Vehicle not found");
        }

        return vehicleResponseList;
    }

    @Override
    public VehicleResponse editVehicle(VehicleRequest vehicleRequest, String garageId, String vehicleId) {
        VehicleResponse vehicleResponse = vehicleRepository.findFirstByGarageIdAndVehicleId(garageId, vehicleId);
        if (vehicleResponse == null) {
            throw new ResourcesNotFoundException("Vehicle not found");
        }

        vehicleResponse  = vehicleResponseMapper.map(vehicleRequest, garageId, vehicleId);

        return vehicleRepository.save(vehicleResponse);
    }
}
