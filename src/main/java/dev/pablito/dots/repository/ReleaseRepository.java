package dev.pablito.dots.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import dev.pablito.dots.entity.DatabaseRelease;


public interface ReleaseRepository extends MongoRepository<DatabaseRelease, String> {

}
