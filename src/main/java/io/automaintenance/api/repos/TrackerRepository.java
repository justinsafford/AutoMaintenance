package io.automaintenance.api.repos;

import io.automaintenance.api.tracker.TrackerEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TrackerRepository extends MongoRepository<TrackerEntity, String>{
    List<TrackerEntity> findByVehicleId(String vId);
}
