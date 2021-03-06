package io.automaintenance.api.vehicle;

import io.automaintenance.api.ResourcesNotFoundException;
import io.automaintenance.api.repos.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VehicleController {
    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    VehicleService vehicleService;

    @RequestMapping(
            value = "/garages/{garageId}/vehicles",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleResponse addNewVehicleToGarage(@PathVariable String garageId,
                                                 @RequestBody VehicleRequest vehicleRequest) {
        return vehicleService.addVehicle(vehicleRequest, garageId);
    }

    @RequestMapping(
            value = "/garages/{garageId}/vehicles/{vehicleId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public VehicleResponse findVehicleInGarage(@PathVariable String garageId,
                                               @PathVariable String vehicleId) {
        return vehicleService.findVehicle(garageId, vehicleId);
    }

    @RequestMapping(
            value = "/garages/{garageId}/vehicles",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<VehicleResponse> findAllVehiclesInGarage(@PathVariable String garageId) {
        return vehicleService.findAllVehicles(garageId);
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

    //TODO: Think this should be patch?
    @RequestMapping(
            value = "/garages/{garageId}/vehicles/{vehicleId}",
            method = RequestMethod.PUT
    )
    @ResponseStatus(HttpStatus.ACCEPTED)
    public VehicleResponse updateVehicle(@PathVariable String garageId,
                                         @PathVariable String vehicleId,
                                         @RequestBody VehicleRequest vehicleRequest) {
        return vehicleService.editVehicle(vehicleRequest, garageId, vehicleId);
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
