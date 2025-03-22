package dev.pablito.dots.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import dev.pablito.dots.entity.DatabaseListing;
import dev.pablito.dots.entity.DatabaseProvider;

@Repository
public interface ProviderRepository extends MongoRepository<DatabaseProvider, String>{
	
	List<DatabaseProvider> findByReleaseId(Long releaseId);

}
