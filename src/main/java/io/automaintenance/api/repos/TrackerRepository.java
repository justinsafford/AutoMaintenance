package io.automaintenance.api.repos;

import io.automaintenance.api.tracker.TrackerEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TrackerRepository extends MongoRepository<TrackerEntity, String>{
}
