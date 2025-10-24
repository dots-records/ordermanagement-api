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
import java.util.Map;


@RestController
@CrossOrigin
@RequestMapping("/dots")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
	
	// Gets order identified by id from database "Orders"
	@Timed
	@GetMapping("/orders/{id}")
	public ResponseEntity<DatabaseOrder>getOrder(@PathVariable String id) throws IOException, InterruptedException {
        try {
        	DatabaseOrder response = orderService.getOrder(id);
            return new ResponseEntity<DatabaseOrder>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("[TASK ERROR] getOrder({})", id, e);
            return ResponseEntity.noContent().build();
        }
	}
		
	@Timed
	@GetMapping("/orders")
	public ResponseEntity<Page<DatabaseOrder>>getOrders(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "50") int size,
			@RequestParam(required = false) Boolean archived,
			@RequestParam(required = false) String search)  {
        try {
        	Page<DatabaseOrder> response;
        	if(search == null) {
        		if(archived == null) {
            		response = orderService.getOrders(page, size);
            	} else {
            		response = orderService.getOrdersByArchived(page, size, archived);
            	}
        	} else {
        		if(archived == null) {
            		response = orderService.searchOrders(search, page, size);
            	} else {
            		response = orderService.searchOrdersByArchived(search, page, size, archived);
            	}
        	}
        	
            return new ResponseEntity<Page<DatabaseOrder>>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("[TASK ERROR] getOrders({}, {}, {}, {}) ", page, size, archived, search, e);
            return ResponseEntity.noContent().build();
        }		
	}
	
	@Timed
	@PatchMapping("/orders/{id}/status")
	public ResponseEntity<Void> patchOrderStatus(
        @PathVariable String id,
        @RequestBody Map<String, String> body) {
	    try {
	        String status = body.get("status");
	        orderService.updateOrderStatusInDatabase(id, status);
	        DatabaseOrder dbOrder = orderService.getOrder(id);
	        orderService.updateOrderStatusInDiscogs(dbOrder.getDiscogsId(), status);
	        return ResponseEntity.noContent().build();
	    } catch (Exception e) {
	        logger.error("[TASK ERROR] patchOrderStatus({}, body={})", id, body, e);
	        return ResponseEntity.internalServerError().build();
	    }
	}
	
	@Timed
	@GetMapping("/getOrdersInformation")
	public ResponseEntity<String>getOrdersInformation() throws Exception {
		return new ResponseEntity<String>(orderService.getOrdersInformation(), HttpStatus.OK);
		
	}
	
	
	
	
	
	
	
	
	
}
