package autoMaintProgram;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccidentRepository extends MongoRepository<AccidentEntity, String>{
}
