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
    VehicleResponseMapper vehicleResponseMapper;

    @Autowired
    public VehicleController(GarageRepository garageRepository, VehicleRepository vehicleRepository, VehicleResponseMapper vehicleResponseMapper) {
        this.garageRepository = garageRepository;
        this.vehicleRepository = vehicleRepository;
        this.vehicleResponseMapper = vehicleResponseMapper;
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

        String vehicleId = UUID.randomUUID().toString();
        VehicleEntity vehicleEntityResponse =
                vehicleResponseMapper.map(vehicleRequest, garageId, vehicleId);

        vehicleRepository.save(vehicleEntityResponse);

        return vehicleEntityResponse;
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
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<VehicleEntity> findAllVehiclesInGarage(@PathVariable String garageId) {

        //TODO:Check this logic.. Should check for vehicle existence first
        List<VehicleEntity> vehicleEntityList = vehicleRepository.findAllByGarageId(garageId);
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
        if (vehicleRepository.findFirstByGarageIdAndVehicleId(garageId, vehicleId) == null) {
            throw new ResourcesNotFoundException("Vehicle not found");
        }
        vehicleRepository.delete(vehicleId);
    }

    @RequestMapping(
            value = "/garages/{garageId}/vehicles/{vehicleId}",
            method = RequestMethod.PUT
    )
    @ResponseStatus(HttpStatus.ACCEPTED)
    public VehicleEntity updateVehicle(@PathVariable String garageId,
                                       @PathVariable String vehicleId,
                                       @RequestBody VehicleRequest vehicleRequest) {

        VehicleEntity vehicleEntity = vehicleRepository.findFirstByGarageIdAndVehicleId(garageId, vehicleId);
        if (vehicleEntity == null) {
            throw new ResourcesNotFoundException("Vehicle not found");
        }

        VehicleEntity vehicleResponseEntity = vehicleResponseMapper.map(vehicleRequest, garageId, vehicleId);

        vehicleRepository.save(vehicleResponseEntity);
        return vehicleResponseEntity;
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
