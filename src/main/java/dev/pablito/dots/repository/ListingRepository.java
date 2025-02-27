package dev.pablito.dots.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import dev.pablito.dots.entity.DatabaseListing;

@Repository
public interface ListingRepository extends MongoRepository<DatabaseListing, String> {

}
