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
            value = "/garages",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public GarageEntity addNewGarage(@RequestBody GarageEntity garageEntity) {

        garageEntity.setGarageId(UUID.randomUUID());
        garageRepository.save(garageEntity);

        return garageEntity;
    }
}
