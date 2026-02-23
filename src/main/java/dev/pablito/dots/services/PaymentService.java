package dev.pablito.dots.services;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.pablito.dots.entity.DatabasePayment;
import dev.pablito.dots.entity.PaymentRequest;
import dev.pablito.dots.repository.PaymentRepository;

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

}
