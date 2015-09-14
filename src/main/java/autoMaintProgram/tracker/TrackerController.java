package autoMaintProgram.tracker;

import autoMaintProgram.ResourcesNotFoundException;
import autoMaintProgram.repos.TrackerRepository;
import autoMaintProgram.repos.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class    TrackerController {
    VehicleRepository vehicleRepository;
    TrackerRepository trackerRepository;

    @Autowired
    public TrackerController(VehicleRepository vehicleRepository, TrackerRepository trackerRepository) {
        this.vehicleRepository = vehicleRepository;
        this.trackerRepository = trackerRepository;
    }

    @RequestMapping(
            value = "/vehicles/{vehicleId}/tracker",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public TrackerEntity addNewTrackerItem(@PathVariable String vehicleId,
                                           @RequestBody TrackerEntity trackerEntity){

        if (vehicleRepository.findOne(vehicleId) == null) {
            throw new ResourcesNotFoundException("Vehicle not found");
        }

        trackerEntity.setVehicleId(vehicleId);
        trackerRepository.save(trackerEntity);

        return trackerEntity;
    }

    @RequestMapping(
            value = "/vehicles//tracker", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public TrackerEntity addNewTrackerItemWithMissingVehicleId(){
            throw new ResourcesNotFoundException("Vehicle not found");
    }
}
