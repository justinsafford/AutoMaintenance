package io.automaintenance.api.repos;

import io.automaintenance.api.garage.GarageEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GarageRepository extends MongoRepository<GarageEntity, String>{
}
