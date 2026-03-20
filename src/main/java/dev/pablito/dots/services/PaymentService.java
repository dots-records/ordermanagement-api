package dev.pablito.dots.services;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.pablito.dots.aop.Timed;
import dev.pablito.dots.entity.DatabasePayment;
import dev.pablito.dots.entity.PaymentRequest;
import dev.pablito.dots.exceptions.MongoException;
import dev.pablito.dots.exceptions.NotFoundException;
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

	public DatabasePayment getPayment(String id) throws NotFoundException {
		Optional<DatabasePayment> optPayment = paymentRepository.findById(id);
		if (optPayment.isPresent()) {
			return optPayment.get();
		} else {
			return null;
		}
	}

	@Timed
	@Transactional(rollbackFor = Exception.class)
	public void createPayment(String orderId, PaymentRequest request) throws MongoException {
		DatabasePayment payment = new DatabasePayment(orderId, request.getCost(), request.getPayout(), getActualDate(),
				request.getReason());
		try {
			paymentRepository.save(payment);
		} catch (Exception e) {
			throw new MongoException("Error creating payment in MongoDB: " + e.getMessage());
		}
	}

	@Timed
	@Transactional(rollbackFor = Exception.class)
	public void createPayment(PaymentRequest request) throws MongoException {
		DatabasePayment payment = new DatabasePayment(request.getCost(), request.getPayout(), getActualDate(),
				request.getReason());
		try {
			paymentRepository.save(payment);
		} catch (Exception e) {
			throw new MongoException("Error creating payment in MongoDB: " + e.getMessage());
		}
	}

}
