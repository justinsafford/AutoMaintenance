package io.automaintenance.api.vehicle;

import io.automaintenance.api.ResourcesNotFoundException;
import io.automaintenance.api.repos.GarageRepository;
import io.automaintenance.api.repos.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VehicleController {
    GarageRepository garageRepository;
    VehicleRepository vehicleRepository;
    VehicleResponseMapper vehicleResponseMapper;

    @Autowired
    VehicleService vehicleService;

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
    public VehicleResponse addNewVehicleToGarage(@PathVariable String garageId,
                                               @RequestBody VehicleRequest vehicleRequest) {

        VehicleResponse vehicleResponseResponse
                = vehicleService.addNewVehicle(vehicleRequest, garageId);

        vehicleRepository.save(vehicleResponseResponse);

        return vehicleResponseResponse;
    }

    @RequestMapping(
            value = "/garages/{garageId}/vehicles/{vehicleId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public VehicleResponse findVehicleInGarage(@PathVariable String garageId,
                                             @PathVariable String vehicleId) {

        VehicleResponse vehicleResponse = vehicleRepository.findFirstByGarageIdAndVehicleId(garageId, vehicleId);
        if (vehicleResponse == null) {
            throw new ResourcesNotFoundException("Vehicle not found");
        }

        return vehicleResponse;
    }

    @RequestMapping(
            value = "/garages/{garageId}/vehicles",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<VehicleResponse> findAllVehiclesInGarage(@PathVariable String garageId) {

        //TODO:Check this logic.. Should check for vehicle existence first
        List<VehicleResponse> vehicleResponseList = vehicleRepository.findAllByGarageId(garageId);
        if (vehicleResponseList == null) {
            throw new ResourcesNotFoundException("Vehicle not found");
        }
        return vehicleResponseList;
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
    public VehicleResponse updateVehicle(@PathVariable String garageId,
                                       @PathVariable String vehicleId,
                                       @RequestBody VehicleRequest vehicleRequest) {

        VehicleResponse vehicleResponse = vehicleRepository.findFirstByGarageIdAndVehicleId(garageId, vehicleId);
        if (vehicleResponse == null) {
            throw new ResourcesNotFoundException("Vehicle not found");
        }

        VehicleResponse vehicleResponseEntity = vehicleResponseMapper.map(vehicleRequest, garageId, vehicleId);

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
