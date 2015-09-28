package io.automaintenance.api.repos;

import io.automaintenance.api.accident.AccidentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AccidentRepository extends MongoRepository<AccidentEntity, String>{
    List<AccidentEntity> findAllByVehicleId(String vId);
}
