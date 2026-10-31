package dev.pablito.dots.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.pablito.dots.aop.Timed;
import dev.pablito.dots.entity.DatabasePayment;
import dev.pablito.dots.entity.PaymentRequest;
import dev.pablito.dots.services.PaymentService;

@RestController
@CrossOrigin
@RequestMapping("/dots")
public class PaymentController {
	@Autowired
	private PaymentService paymentService;

	private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

	@Timed
	@GetMapping("/payments/{id}")
	public ResponseEntity<?> getPayment(@PathVariable String id) throws IOException, InterruptedException {
		try {
			DatabasePayment response = paymentService.getPayment(id);
			return new ResponseEntity<DatabasePayment>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("[TASK ERROR] getPayment({})", id, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@Timed
	@GetMapping("/payments/{year}/{month}")
	public ResponseEntity<?> getPaymentsByDate(@PathVariable int year, @PathVariable int month)
			throws IOException, InterruptedException {
		try {
			if (month < 1 || month > 12) {
				return ResponseEntity.badRequest().body("El mes debe estar entre 1 y 12");
			}

			List<DatabasePayment> response = paymentService.getPaymentsByMonth(year, month);
			return new ResponseEntity<List<DatabasePayment>>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("[TASK ERROR] getPaymentsByDate({}, {})", year, month, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@Timed
	@PostMapping("/orders/{orderId}/payments")
	public ResponseEntity<?> createPaymentAssociatedToOrder(@RequestBody PaymentRequest request,
			@PathVariable String orderId) {
		try {
			paymentService.createPayment(orderId, request);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			logger.error("[TASK ERROR] createPaymentAssociatedToOrder({} {})", request, orderId, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@Timed
	@PostMapping("/payments")
	public ResponseEntity<?> createPayment(@RequestBody PaymentRequest request, @PathVariable String orderId) {
		try {
			paymentService.createPayment(request);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			logger.error("[TASK ERROR] createListing({})", request, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

}
