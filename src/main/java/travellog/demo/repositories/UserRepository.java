package travellog.demo.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import travellog.demo.models.User;

public interface UserRepository extends MongoRepository<User, String>{

    Optional<User> findByEmail(String email);
    
}
