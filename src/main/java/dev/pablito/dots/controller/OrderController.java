package dev.pablito.dots.controller;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.pablito.dots.aop.Timed;
import dev.pablito.dots.entity.DatabaseOrder;
import dev.pablito.dots.entity.OrderRequest;
import dev.pablito.dots.services.OrderService;

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
	public ResponseEntity<?> getOrder(@PathVariable String id) throws IOException, InterruptedException {
		try {
			DatabaseOrder response = orderService.getOrder(id);
			return new ResponseEntity<DatabaseOrder>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("[TASK ERROR] getOrder({})", id, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@Timed
	@GetMapping("/orders")
	public ResponseEntity<?> getOrders(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "50") int size, @RequestParam(required = false) Boolean archived,
			@RequestParam(required = false) String search) {
		try {
			Page<DatabaseOrder> response;
			if (search == null) {
				if (archived == null) {
					response = orderService.getOrders(page, size);
				} else {
					response = orderService.getOrdersByArchived(page, size, archived);
				}
			} else {
				if (archived == null) {
					response = orderService.searchOrders(search, page, size);
				} else {
					response = orderService.searchOrdersByArchived(search, page, size, archived);
				}
			}

			return new ResponseEntity<Page<DatabaseOrder>>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("[TASK ERROR] getOrders({}, {}, {}, {}) ", page, size, archived, search, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@Timed
	@PostMapping("/orders")
	public ResponseEntity<?> createOrder(@RequestBody OrderRequest request) {
		try {
			orderService.createOrder(request);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			logger.error("[TASK ERROR] getOrders({}, {}, {}, {}) ");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@Timed
	@PatchMapping("/orders/{id}/status")
	public ResponseEntity<?> patchOrderStatus(@PathVariable String id, @RequestBody Map<String, String> body) {
		try {
			String status = body.get("status");
			orderService.updateOrderStatusInDatabase(id, status);
			DatabaseOrder dbOrder = orderService.getOrder(id);
			orderService.updateOrderStatusInDiscogs(dbOrder.getDiscogsId(), status);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			logger.error("[TASK ERROR] patchOrderStatus({}, body={})", id, body, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@Timed
	@PatchMapping("/orders/{id}/paymentId")
	public ResponseEntity<?> patchOrderPaymentId(@PathVariable String id, @RequestBody Map<String, String> body) {
		try {
			String paymentId = body.get("paymentId");
			orderService.updateOrderPaymentId(id, paymentId);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			logger.error("[TASK ERROR] patchOrderStatus({}, body={})", id, body, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@Timed
	@PatchMapping("/orders/{id}/justAdded")
	public ResponseEntity<?> patchOrderJustAdded(@PathVariable String id, @RequestBody Map<String, String> body) {
		try {
			String justAddedStr = body.get("justAdded");

			if (justAddedStr == null
					|| (!justAddedStr.equalsIgnoreCase("true") && !justAddedStr.equalsIgnoreCase("false"))) {
				return ResponseEntity.badRequest().body(null);
			}
			boolean justAdded = Boolean.parseBoolean(justAddedStr);
			orderService.updateOrderJustAdded(id, justAdded);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			logger.error("[TASK ERROR] patchOrderJustAdded({}, body={})", id, body, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@Timed
	@PatchMapping("/orders/{id}/warning")
	public ResponseEntity<?> patchOrderWarning(@PathVariable String id, @RequestBody Map<String, String> body) {
		try {
			String warning = body.get("warning");
			orderService.updateOrderWarning(id, warning);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			logger.error("[TASK ERROR] patchOrderWarning({}, body={})", id, body, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@Timed
	@PatchMapping("/orders/{id}/information")
	public ResponseEntity<?> patchOrderInformation(@PathVariable String id, @RequestBody Map<String, String> body) {
		try {
			String information = body.get("information");
			orderService.updateOrderInformation(id, information);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			logger.error("[TASK ERROR] patchOrderInformation({}, body={})", id, body, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@Timed
	@PatchMapping("/orders/{id}/items")
	public ResponseEntity<?> patchOrderItems(@PathVariable String id, @RequestBody Map<String, String> body) {
		try {
		} catch (Exception e) {
			logger.error("[TASK ERROR] patchOrderItems({}, body={})", id, body, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return null;
	}

	@Timed
	@GetMapping("/getOrdersInformation")
	public ResponseEntity<String> getOrdersInformation() throws Exception {
		return new ResponseEntity<String>(orderService.getOrdersInformation(), HttpStatus.OK);
	}

}
