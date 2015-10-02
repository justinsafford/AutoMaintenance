package io.automaintenance.api.accident;

import io.automaintenance.api.ResourcesNotFoundException;
import io.automaintenance.api.repos.AccidentRepository;
import io.automaintenance.api.repos.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        accidentEntity.setVehicleId(vehicleId);
        accidentRepository.save(accidentEntity);

        return accidentEntity;
    }

    @RequestMapping(
            value = "/vehicles/{vehicleId}/accidents",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public List<AccidentEntity> retrieveAllAccidentsByVehicle(@PathVariable String vehicleId) {
        List<AccidentEntity> accidentEntityList =
                accidentRepository.findAllByVehicleId(vehicleId);

        if (accidentEntityList == null) {
            throw new ResourcesNotFoundException("Accidents not found");
        }

        return accidentEntityList;
    }

    @RequestMapping(value = "/vehicles//accidents", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void addNewAccidentMissingVehicleId() {
        throw new ResourcesNotFoundException("Vehicle not found");
    }

    @RequestMapping(value = "/vehicles//accidents", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void retrieveAccidentsWithMissingVehicleId() {
        throw new ResourcesNotFoundException("Vehicle is required");
    }
}
