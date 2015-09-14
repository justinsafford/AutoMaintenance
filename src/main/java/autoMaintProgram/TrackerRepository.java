package autoMaintProgram;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TrackerRepository extends MongoRepository<TrackerEntity, String>{
}
