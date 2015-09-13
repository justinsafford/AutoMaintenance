package autoMaintProgram.maintenance;

import autoMaintProgram.ResourcesNotFoundException;
import autoMaintProgram.repos.MaintenanceRepository;
import autoMaintProgram.repos.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class MaintenanceController {
    VehicleRepository vehicleRepository;
    MaintenanceRepository maintenanceRepository;

    @Autowired
    public MaintenanceController(VehicleRepository vehicleRepository, MaintenanceRepository maintenanceRepository) {
        this.vehicleRepository = vehicleRepository;
        this.maintenanceRepository = maintenanceRepository;
    }

    @RequestMapping(
            value = "/vehicles/{vehicleId}/maintenance",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public MaintenanceEntity addNewMaintenance(@PathVariable String vehicleId,
                                               @RequestBody MaintenanceEntity maintenanceEntity) {
        if (vehicleRepository.findOne(vehicleId) == null) {
            throw new ResourcesNotFoundException("Vehicle not found");
        }
        maintenanceEntity.setVehicleId(vehicleId);
        maintenanceRepository.save(maintenanceEntity);

        return maintenanceEntity;
    }

    @RequestMapping(value = "/vehicles//maintenance", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void addNewMaintenanceMissingVehicleId() {
        throw new ResourcesNotFoundException("Vehicle not found");
    }
}
