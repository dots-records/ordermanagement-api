package dev.pablito.dots.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import dev.pablito.dots.entity.DatabaseRelease;

public interface ReleaseRepository extends MongoRepository<DatabaseRelease, Long> {

	@Query("{ '$or' : [ " + "{ 'title' : { $regex: ?0, $options: 'i' } }, "
			+ "{ 'thumb' : { $regex: ?0, $options: 'i' } }, " + "{ 'artists.name' : { $regex: ?0, $options: 'i' } }, "
			+ "{ 'formats.name' : { $regex: ?0, $options: 'i' } }, "
			+ "{ 'formats.descriptions' : { $regex: ?0, $options: 'i' } } " + "] }")
	Page<DatabaseRelease> findBySearchTerm(String palabra, Pageable pageable);
}
