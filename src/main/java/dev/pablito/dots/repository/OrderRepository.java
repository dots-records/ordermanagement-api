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

	@Query("{ 'archived' : ?0, " + "'$or' : [ " + "{ 'type' : { $regex: ?1, $options: 'i' } }, "
			+ "{ 'number' : { $regex: ?1, $options: 'i' } }, " + "{ 'status' : { $regex: ?1, $options: 'i' } }, "
			+ "{ 'created' : { $regex: ?1, $options: 'i' } }, "
			+ "{ 'delivery_date' : { $regex: ?1, $options: 'i' } }, "
			+ "{ 'payment.shipping' : { $regex: ?1, $options: 'i' } }, "
			+ "{ 'payment.items' : { $regex: ?1, $options: 'i' } }, "
			+ "{ 'provider.name' : { $regex: ?1, $options: 'i' } }, "
			+ "{ 'provider.information' : { $regex: ?1, $options: 'i' } }, "
			+ "{ 'items.name' : { $regex: ?1, $options: 'i' } }, "
			+ "{ 'items.artists.name' : { $regex: ?1, $options: 'i' } } " + "] }")
	Page<DatabaseOrder> findByArchivedAndSearchTerm(boolean archived, String palabra, Pageable pageable);

	@Query("{ '$or' : [ " + "{ 'type' : { $regex: ?0, $options: 'i' } }, "
			+ "{ 'id' : { $regex: ?0, $options: 'i' } }, " + "{ 'status' : { $regex: ?0, $options: 'i' } }, "
			+ "{ 'created' : { $regex: ?0, $options: 'i' } }, "
			+ "{ 'delivery_date' : { $regex: ?0, $options: 'i' } }, "
			+ "{ 'payment.shipping' : { $regex: ?0, $options: 'i' } }, "
			+ "{ 'payment.items' : { $regex: ?0, $options: 'i' } }, "
			+ "{ 'provider.name' : { $regex: ?0, $options: 'i' } }, "
			+ "{ 'provider.information' : { $regex: ?0, $options: 'i' } }, "
			+ "{ 'items.name' : { $regex: ?0, $options: 'i' } }, "
			+ "{ 'items.artists.name' : { $regex: ?0, $options: 'i' } } " + "] }")
	Page<DatabaseOrder> findBySearchTerm(String palabra, Pageable pageable);

}
