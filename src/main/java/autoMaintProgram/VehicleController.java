package autoMaintProgram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
    public VehicleEntity addNewVehicle(@PathVariable String garageId,
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
            value = "/garages//vehicles", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void submitVehicleWithMissingGarage() {
        throw new ResourcesNotFoundException("GarageId is required");
    }
}
