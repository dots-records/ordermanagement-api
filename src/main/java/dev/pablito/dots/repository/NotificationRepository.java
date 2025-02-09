package dev.pablito.dots.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import dev.pablito.dots.entity.DatabaseNotification;

@Repository
public interface NotificationRepository extends MongoRepository<DatabaseNotification, String>{

}
