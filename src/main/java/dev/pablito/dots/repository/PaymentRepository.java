package dev.pablito.dots.repository;

import dev.pablito.dots.entity.DatabasePayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends MongoRepository<DatabasePayment, String> {
    @Query("{ '$or' : [ " + "{ 'reason' : { $regex: ?0, $options: 'i' } }, "
            + "{ 'creationDate' : { $regex: ?0, $options: 'i' } } " + "] }")
    Page<DatabasePayment> findBySearchTerm(String term, Pageable pageable);
}
