package autoMaintProgram;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface GarageRepository extends MongoRepository<GarageEntity, String>{
}
