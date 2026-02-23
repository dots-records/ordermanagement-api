package dev.pablito.dots.controller;

import java.io.IOException;

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
	public ResponseEntity<DatabasePayment> getPayment(@PathVariable String id)
			throws IOException, InterruptedException {
		try {
			DatabasePayment response = paymentService.getPayment(id);
			return new ResponseEntity<DatabasePayment>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("[TASK ERROR] getPayment({})", id, e);
			return ResponseEntity.internalServerError().body(null);
		}
	}

	@Timed
	@PostMapping("/orders/{orderId}/payments")
	public ResponseEntity<String> createPayment(@PathVariable String orderId, @RequestBody PaymentRequest request)
			throws IOException, InterruptedException {
		try {
			// Supongamos que createPayment devuelve el ID del payment creado
			String paymentId = paymentService.createPayment(orderId, request);
			return ResponseEntity.ok(paymentId); // devolvemos 200 OK con el ID
		} catch (Exception e) {
			logger.error("[TASK ERROR] createPayment({} {})", orderId, request, e);
			return ResponseEntity.internalServerError().body(null);
		}
	}

	@Timed
	@PostMapping("/payments")
	public void createPayment(@RequestBody PaymentRequest request) throws IOException, InterruptedException {
		try {
			paymentService.createPayment(request);
		} catch (Exception e) {
			logger.error("[TASK ERROR] createListing({})", request, e);
		}
	}

}
