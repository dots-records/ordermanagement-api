package dev.pablito.dots.controller;

import dev.pablito.dots.aop.Timed;
import dev.pablito.dots.entity.DatabasePayment;
import dev.pablito.dots.entity.PaymentRequest;
import dev.pablito.dots.services.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

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

	@Timed
	@DeleteMapping("/payments/{paymentId}")
	public void deletePayment(@PathVariable String paymentId) {
		try {
			paymentService.deletePayment(paymentId);
		} catch (Exception e) {
			logger.error("[TASK ERROR] deletePayment({})", e);
		}
	}

	@Timed
	@PatchMapping("/payments/{paymentId}/cost")
	public ResponseEntity<Void> patchPaymentCost(@PathVariable String paymentId, @RequestBody Map<String, Double> body) {
		try {
			Double cost = body.get("cost");
			paymentService.updatePaymentCost(paymentId, cost);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			logger.error("[TASK ERROR] updatePaymentCost({})", paymentId, body, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	@Timed
	@PatchMapping("/payments/{paymentId}/payout")
	public ResponseEntity<Void> patchPaymentPayout(@PathVariable String paymentId, @RequestBody Map<String, Double> body) {
		try {
			Double payout = body.get("payout");
			paymentService.updatePaymentPayout(paymentId, payout);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			logger.error("[TASK ERROR] updatePaymentPayout({})", paymentId, body, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Timed
	@PatchMapping("/payments/{paymentId}/reason")
	public ResponseEntity<Void> patchPaymentReason
			(@PathVariable String paymentId, @RequestBody Map<String, Double> body) {
		try {
			Double cost = body.get("cost");
			paymentService.updatePaymentCost(paymentId, cost);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			logger.error("[TASK ERROR] updatePaymentCost({})", paymentId, body, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Timed
	@GetMapping("/payments")
	public ResponseEntity<Page<DatabasePayment>> getPayments(@RequestParam(defaultValue = "0") int page,
														 @RequestParam(defaultValue = "50") int size,
														 @RequestParam(required = false) String search) {
		try {
			Page<DatabasePayment> response;
			if (search == null) {
				response = paymentService.getPayments(page, size);
			} else {
				response = paymentService.searchPayments(search, page, size);
			}
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("[TASK ERROR] getPayments({}, {}, {}, {}) ", page, size, search, e);
			return ResponseEntity.noContent().build();
		}
	}

}
