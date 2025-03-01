package dev.pablito.dots.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import dev.pablito.dots.entity.DatabaseListing;
import dev.pablito.dots.entity.DatabaseOrder;

@Repository
public interface ListingRepository extends MongoRepository<DatabaseListing, String> {
	
	List<DatabaseListing> findByReleaseId(Long releaseId);

}
