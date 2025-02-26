package dev.pablito.dots.scheduler;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import dev.pablito.dots.aop.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import dev.pablito.dots.entity.DatabaseOrder;
import dev.pablito.dots.services.OrderService;

@Component
public class OrderScheduler {
	
	@Autowired
	private OrderService orderService;
	
	private static final Logger logger = LoggerFactory.getLogger(OrderScheduler.class);
	
	// TODO: Hacer q cuando falle tambien guarde el ordersInformation
	//Every 60 seconds updates the database "Orders" checking if a new Order has been made in Discogs
	@Timed
	//@Scheduled(fixedRate = 60000)
	public void checkOrdersInDiscogs() {
        try {
        	orderService.checkOrdersInDiscogs();
        } catch (Exception e) {
            logger.error("[SCHEDULED ERROR] checkOrdersInDiscogs() ", e);
        }
		
	}
	

}
