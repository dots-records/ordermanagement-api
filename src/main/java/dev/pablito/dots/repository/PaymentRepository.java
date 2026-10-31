package dev.pablito.dots.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import dev.pablito.dots.entity.DatabasePayment;

@Repository
public interface PaymentRepository extends MongoRepository<DatabasePayment, String> {
	Optional<DatabasePayment> findFirstByReason(String reason);

	@Query("{ 'creationDate': { $gte: ?0, $lt: ?1 } }")
	List<DatabasePayment> findPaymentsByCreationDateBetween(String start, String end);
}
