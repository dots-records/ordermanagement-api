package dev.pablito.dots.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import dev.pablito.dots.entity.DatabasePayment;

@Repository
public interface PaymentRepository extends MongoRepository<DatabasePayment, String> {

}
