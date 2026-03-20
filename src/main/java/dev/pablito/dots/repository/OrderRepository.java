package dev.pablito.dots.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import dev.pablito.dots.entity.DatabaseOrder;

@Repository
public interface OrderRepository extends MongoRepository<DatabaseOrder, String> {

	Optional<DatabaseOrder> findOrderById(String id);

	List<DatabaseOrder> findByStatus(String status);

	List<DatabaseOrder> findByArchived(boolean archived);

	Page<DatabaseOrder> findByArchived(boolean archived, Pageable pageable);

	List<DatabaseOrder> findByStatusAndArchived(String status, boolean archived);

	List<DatabaseOrder> findByStatusAndArchivedAndPlatform(String status, boolean archived, String platform);

	// 🔎 SEARCH CON ARCHIVED
	@Query("{ 'archived' : ?0, " + "'$or' : [ " + "{ 'discogsId' : { $regex: ?1, $options: 'i' } }, "
			+ "{ 'status' : { $regex: ?1, $options: 'i' } }, " + "{ 'created' : { $regex: ?1, $options: 'i' } }, "
			+ "{ 'information' : { $regex: ?1, $options: 'i' } }, " +

			"{ 'items.release.name' : { $regex: ?1, $options: 'i' } }, "
			+ "{ 'items.release.artists.name' : { $regex: ?1, $options: 'i' } }, " +

			"{ 'items.provider.description' : { $regex: ?1, $options: 'i' } }, "
			+ "{ 'items.provider.discCondition' : { $regex: ?1, $options: 'i' } }, "
			+ "{ 'items.provider.sleeveCondition' : { $regex: ?1, $options: 'i' } }, " +

			"{ 'items.listing.platform' : { $regex: ?1, $options: 'i' } } " + "] }")
	Page<DatabaseOrder> findByArchivedAndSearchTerm(boolean archived, String palabra, Pageable pageable);

	// 🔎 SEARCH GLOBAL
	@Query("{ '$or' : [ " + "{ 'discogsId' : { $regex: ?0, $options: 'i' } }, "
			+ "{ 'id' : { $regex: ?0, $options: 'i' } }, " + "{ 'status' : { $regex: ?0, $options: 'i' } }, "
			+ "{ 'created' : { $regex: ?0, $options: 'i' } }, " + "{ 'information' : { $regex: ?0, $options: 'i' } }, "
			+

			"{ 'items.release.name' : { $regex: ?0, $options: 'i' } }, "
			+ "{ 'items.release.artists.name' : { $regex: ?0, $options: 'i' } }, " +

			"{ 'items.provider.description' : { $regex: ?0, $options: 'i' } }, "
			+ "{ 'items.provider.discCondition' : { $regex: ?0, $options: 'i' } }, "
			+ "{ 'items.provider.sleeveCondition' : { $regex: ?0, $options: 'i' } }, " +

			"{ 'items.listing.platform' : { $regex: ?0, $options: 'i' } } " + "] }")
	Page<DatabaseOrder> findBySearchTerm(String palabra, Pageable pageable);
}