package dev.pablito.dots.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import dev.pablito.dots.entity.DatabaseListing;

@Repository
public interface ListingRepository extends MongoRepository<DatabaseListing, String> {

	List<DatabaseListing> findByReleaseIdAndProviderId(Long releaseId, String providerId);

	Optional<DatabaseListing> findByIdAndReleaseIdAndProviderId(String listingId, Long releaseId, String providerId);

}
