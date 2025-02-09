package dev.pablito.dots.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import dev.pablito.dots.api.discogs.DiscogsOrder;
import dev.pablito.dots.entity.DatabaseOrder;
import dev.pablito.dots.entity.Message;
import dev.pablito.dots.entity.SearchRequest;
import dev.pablito.dots.scheduler.OrderScheduler;
import dev.pablito.dots.services.OrderService;


@RestController
@CrossOrigin
@RequestMapping("/dots")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
		
	// Gets orders which status = {Payment Received, Invoice Sent, Payment Pending} and archived = false from database "Orders"
	@GetMapping("/getUnarchivedNewOrders")
	public ResponseEntity<List<DatabaseOrder>>getUnarchivedNewOrders()  {
		Instant start = Instant.now();
        logger.info("[TASK START] getUnarchivedNewOrders()");
        try {
        	List<DatabaseOrder> response = orderService.getUnarchivedNewOrders();
            Instant end = Instant.now();
            long duration = Duration.between(start, end).toSeconds();
            logger.info("[TASK END] getUnarchivedNewOrders() - {} s ", duration);
            return new ResponseEntity<List<DatabaseOrder>>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("[TASK ERROR] getUnarchivedNewOrders() ", e);
            return ResponseEntity.noContent().build();
        }
		
	}
	
	// Gets orders which archived = false from database "Orders"
	@GetMapping("/getUnarchivedOrders/page={page}&size={size}")
	public ResponseEntity<Page<DatabaseOrder>>getUnarchivedOrders(@PathVariable int page, @PathVariable int size) {		
		Instant start = Instant.now();
        logger.info("[TASK START] getUnarchivedOrders({}, {})", page, size);
        try {
        	Page<DatabaseOrder> response = orderService.getUnarchivedOrders(page, size);
            Instant end = Instant.now();
            long duration = Duration.between(start, end).toSeconds();
            logger.info("[TASK END] getUnarchivedOrders({}, {}) - {} s ", page, size, duration);
            return new ResponseEntity<Page<DatabaseOrder>>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("[TASK ERROR] getUnarchivedOrders({}, {})", page, size, e);
            return ResponseEntity.noContent().build();
        }
	}  
	
	// Gets orders which archived = true from database "Orders"
	@GetMapping("/getArchivedOrders/page={page}&size={size}")
	public ResponseEntity<Page<DatabaseOrder>>getArchivedOrders(@PathVariable int page, @PathVariable int size) {
		Instant start = Instant.now();
        logger.info("[TASK START] getArchivedOrders({}, {})", page, size);
        try {
        	Page<DatabaseOrder> response = orderService.getArchivedOrders(page, size);
            Instant end = Instant.now();
            long duration = Duration.between(start, end).toSeconds();
            logger.info("[TASK END] getArchivedOrders({}, {}) - {} s ", page, size, duration);
            return new ResponseEntity<Page<DatabaseOrder>>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("[TASK ERROR] getArchivedOrders({}, {})", page, size, e);
            return ResponseEntity.noContent().build();
        }
	}
	
	// Gets all orders from database "Orders"
	@GetMapping("/getAllOrders/page={page}&size={size}")
	public ResponseEntity<Page<DatabaseOrder>>getAllOrders(@PathVariable int page, @PathVariable int size) throws IOException, InterruptedException {
		Instant start = Instant.now();
        logger.info("[TASK START] getAllOrders({}, {})", page, size);
        try {
        	Page<DatabaseOrder> response = orderService.getAllOrders(page, size);
            Instant end = Instant.now();
            long duration = Duration.between(start, end).toSeconds();
            logger.info("[TASK END] getAllOrders({}, {}) - {} s ", page, size, duration);
            return new ResponseEntity<Page<DatabaseOrder>>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("[TASK ERROR] getAllOrders({}, {})", page, size, e);
            return ResponseEntity.noContent().build();
        }
	}
	
	// Gets order identified by id from database "Orders"
	@GetMapping("/getOrder/{id}")
	public ResponseEntity<DatabaseOrder>getOrder(@PathVariable String id) throws IOException, InterruptedException {
		Instant start = Instant.now();
        logger.info("[TASK START] getOrder({})", id);
        try {
        	DatabaseOrder response = orderService.getOrder(id);
            Instant end = Instant.now();
            long duration = Duration.between(start, end).toSeconds();
            logger.info("[TASK END] getOrder({}) - {} s ", id, duration);
            return new ResponseEntity<DatabaseOrder>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("[TASK ERROR] getOrder({})", id, e);
            return ResponseEntity.noContent().build();
        }
	}
	
	// TODO: Hacer que devuelva algo
	// Updates status of order identified by id in database "Orders" and Discogs to newStatus 
	@PostMapping("/updateStatusOrder/{id}/{newStatus}")
	public void updateStatusOrder(@PathVariable String id, @PathVariable String newStatus) {
		Instant start = Instant.now();
        logger.info("[TASK START] updateStatusOrder({}, {})", id, newStatus);
        try {
        	orderService.updateOrderStatusInDiscogs(id, newStatus);
    		orderService.updateOrderStatusInDatabase(id, newStatus);
            Instant end = Instant.now();
            long duration = Duration.between(start, end).toSeconds();
            logger.info("[TASK END] updateStatusOrder({}, {}) - {} s ", id, newStatus, duration);
        } catch (Exception e) {
            logger.error("[TASK ERROR] updateStatusOrder({}, {})", id, newStatus, e);
        }
        
	}
	
	// TODO: Falta poner el log
	// Search orders which archived = false in database "Orders"
	@PostMapping("/searchUnarchivedOrders/page={page}&size={size}")
	public ResponseEntity<Page<DatabaseOrder>>searchUnarchivedOrders(@RequestBody SearchRequest request, @PathVariable int page, @PathVariable int size) throws IOException, InterruptedException {
		System.out.println(request.getSearch());
		return new ResponseEntity<Page<DatabaseOrder>>(orderService.searchOrdersByArchived(request.getSearch(), page, size, false), HttpStatus.OK);
	}
	
	@PostMapping("/searchArchivedOrders/page={page}&size={size}")
	public ResponseEntity<Page<DatabaseOrder>>searchArchivedOrders(@RequestBody SearchRequest request, @PathVariable int page, @PathVariable int size) throws IOException, InterruptedException {
		System.out.println(request.getSearch());
		return new ResponseEntity<Page<DatabaseOrder>>(orderService.searchOrdersByArchived(request.getSearch(), page, size, true), HttpStatus.OK);
	}
	
	@PostMapping("/searchAllOrders/page={page}&size={size}")
	public ResponseEntity<Page<DatabaseOrder>>searchAllOrders(@RequestBody SearchRequest request, @PathVariable int page, @PathVariable int size) throws IOException, InterruptedException {
		System.out.println(request.getSearch());
		return new ResponseEntity<Page<DatabaseOrder>>(orderService.searchOrders(request.getSearch(), page, size), HttpStatus.OK);
	}
	
	
	
	// Testing : Releases
	
	
	
	@GetMapping("createListings")
	public ResponseEntity<String> createListings() throws IOException, InterruptedException {
		return new ResponseEntity<String>(orderService.createListings(), HttpStatus.CREATED);
	}
	
	
	
	
	
	
	
}
