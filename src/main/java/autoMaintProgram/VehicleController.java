package autoMaintProgram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class VehicleController {

    @Autowired
    GarageRepository garageRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @RequestMapping(
            value = "/garages/{garageId}/vehicles",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleEntity addNewVehicleToGarage(@PathVariable String garageId,
                                               @RequestBody VehicleRequest vehicleRequest) {

        if (garageRepository.findOne(garageId) == null) {
            throw new ResourcesNotFoundException("GarageId not found");
        }

        VehicleEntity vehicleEntity = new VehicleEntity();
        vehicleEntity.setVehicleId(UUID.randomUUID().toString());
        vehicleEntity.setGarageId(garageId);
        vehicleEntity.setVehicleName(vehicleRequest.getVehicleName());
        vehicleEntity.setVehicleYear(vehicleRequest.getVehicleYear());
        vehicleEntity.setVehicleMake(vehicleRequest.getVehicleMake());
        vehicleEntity.setVehicleModel(vehicleRequest.getVehicleModel());

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
        //TODO:Check this logic..
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
        throw new ResourcesNotFoundException("GarageId is required");
    }

    @RequestMapping(
            value = "/garages//vehicles", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void retrieveVehiclesWithMissingGarage() {
        throw new ResourcesNotFoundException("Garage is required");
    }
}
