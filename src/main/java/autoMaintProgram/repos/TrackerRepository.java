package autoMaintProgram.repos;

import autoMaintProgram.tracker.TrackerEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TrackerRepository extends MongoRepository<TrackerEntity, String>{
}
