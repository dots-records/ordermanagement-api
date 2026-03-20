package dev.pablito.dots.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import dev.pablito.dots.entity.DatabasePayment;

@Repository
public interface PaymentRepository extends MongoRepository<DatabasePayment, String> {
	Optional<DatabasePayment> findFirstByReason(String reason);
}
