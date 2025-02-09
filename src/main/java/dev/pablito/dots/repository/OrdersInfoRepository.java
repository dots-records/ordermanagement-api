package dev.pablito.dots.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import dev.pablito.dots.entity.OrdersInfo;

@Repository
public interface OrdersInfoRepository extends MongoRepository<OrdersInfo, String>{

}
