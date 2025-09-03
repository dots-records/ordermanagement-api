package dev.pablito.dots.controller;

import dev.pablito.dots.aop.Timed;
import dev.pablito.dots.entity.DatabaseOrder;
import dev.pablito.dots.entity.SearchRequest;
import dev.pablito.dots.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Duration;
import java.util.List;


@RestController
@CrossOrigin
@RequestMapping("/dots")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
		
	// Gets orders which status = {Payment Received, Invoice Sent, Payment Pending} and archived = false from database "Orders"
	@Timed
	@GetMapping("/getUnarchivedNewOrders")
	public ResponseEntity<List<DatabaseOrder>>getUnarchivedNewOrders()  {
        try {
        	List<DatabaseOrder> response = orderService.getUnarchivedNewOrders();
            return new ResponseEntity<List<DatabaseOrder>>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("[TASK ERROR] getUnarchivedNewOrders() ", e);
            return ResponseEntity.noContent().build();
        }
		
	}
	
	// Gets orders which archived = false from database "Orders"
	@Timed
	@GetMapping("/getUnarchivedOrders/page={page}&size={size}")
	public ResponseEntity<Page<DatabaseOrder>>getUnarchivedOrders(@PathVariable int page, @PathVariable int size) {		
        try {
        	Page<DatabaseOrder> response = orderService.getUnarchivedOrders(page, size);
            return new ResponseEntity<Page<DatabaseOrder>>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("[TASK ERROR] getUnarchivedOrders({}, {})", page, size, e);
            return ResponseEntity.noContent().build();
        }
	}  
	
	// Gets orders which archived = true from database "Orders"
	@Timed
	@GetMapping("/getArchivedOrders/page={page}&size={size}")
	public ResponseEntity<Page<DatabaseOrder>>getArchivedOrders(@PathVariable int page, @PathVariable int size) {
        try {
        	Page<DatabaseOrder> response = orderService.getArchivedOrders(page, size);
            return new ResponseEntity<Page<DatabaseOrder>>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("[TASK ERROR] getArchivedOrders({}, {})", page, size, e);
            return ResponseEntity.noContent().build();
        }
	}
	
	// Gets all orders from database "Orders"
	@Timed
	@GetMapping("/getAllOrders/page={page}&size={size}")
	public ResponseEntity<Page<DatabaseOrder>>getAllOrders(@PathVariable int page, @PathVariable int size) throws IOException, InterruptedException {
        try {
        	Page<DatabaseOrder> response = orderService.getAllOrders(page, size);
            return new ResponseEntity<Page<DatabaseOrder>>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("[TASK ERROR] getAllOrders({}, {})", page, size, e);
            return ResponseEntity.noContent().build();
        }
	}
	
	// Gets order identified by id from database "Orders"
	@Timed
	@GetMapping("/getOrder/{id}")
	public ResponseEntity<DatabaseOrder>getOrder(@PathVariable String id) throws IOException, InterruptedException {
        try {
        	DatabaseOrder response = orderService.getOrder(id);
            return new ResponseEntity<DatabaseOrder>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("[TASK ERROR] getOrder({})", id, e);
            return ResponseEntity.noContent().build();
        }
	}
	
	// TODO: Hacer que devuelva algo
	// Updates status of order identified by id in database "Orders" and Discogs to newStatus 
	@Timed
	@PostMapping("/updateStatusOrder/{id}/{newStatus}")
	public void updateStatusOrder(@PathVariable String id, @PathVariable String newStatus) {
        try {
        	//orderService.updateOrderStatusInDiscogs(id, newStatus);
    		orderService.updateOrderStatusInDatabase(id, newStatus);
        } catch (Exception e) {
            logger.error("[TASK ERROR] updateStatusOrder({}, {})", id, newStatus, e);
        }
	}
	
	// TODO: Falta poner el log
	// Search orders which archived = false in database "Orders"
	@PostMapping("/searchUnarchivedOrders/page={page}&size={size}")
	@Timed
	public ResponseEntity<Page<DatabaseOrder>>searchUnarchivedOrders(@RequestBody SearchRequest request, @PathVariable int page, @PathVariable int size) throws IOException, InterruptedException {
		logger.info(request.getSearch());
		return new ResponseEntity<Page<DatabaseOrder>>(orderService.searchOrdersByArchived(request.getSearch(), page, size, false), HttpStatus.OK);
	}
	
	// Search orders which archived = true in database "Orders"
	@Timed
	@PostMapping("/searchArchivedOrders/page={page}&size={size}")
	public ResponseEntity<Page<DatabaseOrder>>searchArchivedOrders(@RequestBody SearchRequest request, @PathVariable int page, @PathVariable int size) throws IOException, InterruptedException {
		return new ResponseEntity<Page<DatabaseOrder>>(orderService.searchOrdersByArchived(request.getSearch(), page, size, true), HttpStatus.OK);
	}
	
	// Search orders in database "Orders"
	@Timed
	@PostMapping("/searchAllOrders/page={page}&size={size}")
	public ResponseEntity<Page<DatabaseOrder>>searchAllOrders(@RequestBody SearchRequest request, @PathVariable int page, @PathVariable int size) throws IOException, InterruptedException {
		logger.info(request.getSearch());
		return new ResponseEntity<Page<DatabaseOrder>>(orderService.searchOrders(request.getSearch(), page, size), HttpStatus.OK);
	}
	
	@Timed
	@GetMapping("/getOrdersInformation")
	public ResponseEntity<String>getOrdersInformation() throws Exception {
		return new ResponseEntity<String>(orderService.getOrdersInformation(), HttpStatus.OK);
		
	}
	
	
	
	
	
	
	
	
	
}
