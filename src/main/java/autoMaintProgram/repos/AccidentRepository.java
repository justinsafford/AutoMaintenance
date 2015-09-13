package autoMaintProgram.repos;

import autoMaintProgram.accident.AccidentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccidentRepository extends MongoRepository<AccidentEntity, String>{
}
