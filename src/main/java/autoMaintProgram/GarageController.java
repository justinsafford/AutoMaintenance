package autoMaintProgram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class GarageController {

    GarageRepository garageRepository;

    @Autowired
    public GarageController(GarageRepository garageRepository) {
        this.garageRepository = garageRepository;
    }

    @RequestMapping(
            value = "/garages/{garageId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public GarageEntity retrieveGarage(@PathVariable String garageId) {
        return garageRepository.findOne(garageId);
    }

    @RequestMapping(
            value = "/garages",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public GarageEntity addNewGarage(@RequestBody GarageEntity garageEntity) {

        garageEntity.setGarageId(UUID.randomUUID().toString());
        garageRepository.save(garageEntity);

        return garageEntity;
    }

    @RequestMapping(
            value = "/garages/{garageId}",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public void deleteGarage(@PathVariable String garageId) {

        if(garageRepository.findOne(garageId) == null) {
            throw new ResourcesNotFoundException("Garage not found");
        }
        garageRepository.delete(garageId);

    }
}
