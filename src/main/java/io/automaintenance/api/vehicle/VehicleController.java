package io.automaintenance.api.vehicle;

import io.automaintenance.api.ResourcesNotFoundException;
import io.automaintenance.api.repos.GarageRepository;
import io.automaintenance.api.repos.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class VehicleController {
    GarageRepository garageRepository;
    VehicleRepository vehicleRepository;

    @Autowired
    public VehicleController(GarageRepository garageRepository, VehicleRepository vehicleRepository) {
        this.garageRepository = garageRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @RequestMapping(
            value = "/garages/{garageId}/vehicles",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleEntity addNewVehicleToGarage(@PathVariable String garageId,
                                               @RequestBody VehicleRequest vehicleRequest) {

        if (garageRepository.findOne(garageId) == null) {
            throw new ResourcesNotFoundException("Garage not found");
        }

        VehicleEntity vehicleEntity = new VehicleEntity();
        vehicleEntity.setVehicleId(UUID.randomUUID().toString());
        vehicleEntity.setGarageId(garageId);
        vehicleEntity.setName(vehicleRequest.getName());
        vehicleEntity.setYear(vehicleRequest.getYear());
        vehicleEntity.setMake(vehicleRequest.getMake());
        vehicleEntity.setModel(vehicleRequest.getModel());

        vehicleRepository.save(vehicleEntity);

        return vehicleEntity;
    }

    @RequestMapping(
            value = "/garages/{garageId}/vehicles/{vehicleId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public VehicleEntity findVehicleInGarage(@PathVariable String garageId,
                                             @PathVariable String vehicleId) {

        VehicleEntity vehicleEntity = vehicleRepository.findFirstByGarageIdAndVehicleId(garageId, vehicleId);
        if (vehicleEntity == null) {
            throw new ResourcesNotFoundException("Vehicle not found");
        }

        return vehicleEntity;
    }

    @RequestMapping(
            value = "/garages/{garageId}/vehicles",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)

    public List<VehicleEntity> findAllVehiclesInGarage(@PathVariable String garageId) {

        List<VehicleEntity> vehicleEntityList = vehicleRepository.findAllByGarageId(garageId);
        //TODO:Check this logic.. Should check for vehicle existence first
        if (vehicleEntityList == null) {
            throw new ResourcesNotFoundException("Vehicle not found");
        }
        return vehicleEntityList;
    }

    @RequestMapping(
            value = "/garages/{garageId}/vehicles/{vehicleId}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public void deleteVehicleInGarage(@PathVariable String garageId,
                                      @PathVariable String vehicleId) {
        if(vehicleRepository.findFirstByGarageIdAndVehicleId(garageId, vehicleId) == null) {
            throw new ResourcesNotFoundException("Vehicle not found");
        }
        vehicleRepository.delete(vehicleId);
    }

    @RequestMapping(
            value = "/garages//vehicles", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void submitVehicleWithMissingGarage() {
        throw new ResourcesNotFoundException("Garage is required");
    }

    @RequestMapping(
            value = "/garages//vehicles", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void retrieveVehiclesWithMissingGarage() {
        throw new ResourcesNotFoundException("Garage is required");
    }
}
