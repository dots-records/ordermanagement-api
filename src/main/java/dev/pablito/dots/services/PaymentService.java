package dev.pablito.dots.services;

import dev.pablito.dots.aop.Timed;
import dev.pablito.dots.entity.DatabasePayment;
import dev.pablito.dots.entity.PaymentRequest;
import dev.pablito.dots.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class PaymentService {

	@Autowired
	private PaymentRepository paymentRepository;

	private String getActualDate() {
		ZoneId zoneId = ZoneId.of("America/Los_Angeles");
		ZonedDateTime horaActual = ZonedDateTime.now(zoneId);
		return horaActual.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	}

	public DatabasePayment getPayment(String id) throws IOException, InterruptedException {
		return paymentRepository.findById(id).get();
	}

	public String createPayment(String orderId, PaymentRequest request) throws IOException, InterruptedException {
		DatabasePayment payment = new DatabasePayment(orderId, request.getCost(), request.getPayout(), getActualDate(),
				request.getReason());
		paymentRepository.save(payment);
		return payment.getId();
	}

	public void createPayment(PaymentRequest request) throws IOException, InterruptedException {
		DatabasePayment payment = new DatabasePayment(request.getCost(), request.getPayout(), getActualDate(),
				request.getReason());
		paymentRepository.save(payment);
	}

	public void deletePayment(String paymentId) {
		paymentRepository.deleteById (paymentId);
	}

	public Optional<DatabasePayment> updatePaymentCost (String paymentId, Double cost) {
		return paymentRepository.findById(paymentId).map(payment -> {
			payment.setCost(cost);
			return paymentRepository.save(payment);
		});
	}

	public Optional<DatabasePayment> updatePaymentPayout (String paymentId, Double payout) {
		return paymentRepository.findById(paymentId).map(payment -> {
			payment.setPayout(payout);
			return paymentRepository.save(payment);
		});
	}

	public Optional<DatabasePayment> updatePaymentReason (String paymentId, String reason) {
		return paymentRepository.findById(paymentId).map(payment -> {
			payment.setReason(reason);
			return paymentRepository.save(payment);
		});
	}

	@Timed
	public Page<DatabasePayment> getPayments(int page, int size) throws IOException, InterruptedException {
		PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdComplete"));
		Page<DatabasePayment> paymentsPage = paymentRepository.findAll(pageable);
		return paymentsPage;
	}

	@Timed
	public Page<DatabasePayment> searchPayments(String term, int page, int size) throws IOException, InterruptedException {
		PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdComplete"));
		Page<DatabasePayment> paymentsPage = paymentRepository.findBySearchTerm(term, pageable);
		return paymentsPage;
	}
}
