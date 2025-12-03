package travellog.demo.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import travellog.demo.models.Photo;

public interface PhotoRepository extends MongoRepository<Photo, String>{
    List<Photo> findByTripId(String tripId);
    
}
