package autoMaintProgram;

import autoMaintProgram.repos.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccidentController {
    VehicleRepository vehicleRepository;
    AccidentRepository accidentRepository;

    @Autowired
    public AccidentController(VehicleRepository vehicleRepository, AccidentRepository accidentRepository) {
        this.vehicleRepository = vehicleRepository;
        this.accidentRepository = accidentRepository;
    }

    @RequestMapping(
            value = "/vehicles/{vehicleId}/accidents",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public AccidentEntity addNewAccident(@PathVariable String vehicleId,
                                         @RequestBody AccidentEntity accidentEntity) {
        if (vehicleRepository.findOne(vehicleId) == null) {
            throw new ResourcesNotFoundException("Vehicle not found");
        }
        accidentRepository.save(accidentEntity);

        return accidentEntity;
    }

    @RequestMapping(value = "/vehicles//accidents", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void addNewAccidentMissingVehicleId() {
        throw new ResourcesNotFoundException("Vehicle not found");
    }
}
