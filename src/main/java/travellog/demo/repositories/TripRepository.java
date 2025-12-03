package travellog.demo.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import travellog.demo.models.Trip;
public interface TripRepository extends MongoRepository<Trip, String>{
    List<Trip> findByUserId(String userId);
}
