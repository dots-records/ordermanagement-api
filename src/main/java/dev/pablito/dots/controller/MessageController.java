package dev.pablito.dots.controller;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.pablito.dots.entity.MessageRequest;
import dev.pablito.dots.scheduler.MessagesScheduler;
import dev.pablito.dots.services.MessageService;
import dev.pablito.dots.services.OrderService;

@RestController
@CrossOrigin
@RequestMapping("/dots")
public class MessageController {
	@Autowired
	private MessageService messageService;
	@Autowired
	private OrderService orderService;
	private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
	
	// Resets to zero the atributes newMessages of the order identified by id
	@PostMapping("resetNewMessages/{id}")
	public void resetNewMessages(@PathVariable String id)  {
		Instant start = Instant.now();
        logger.info("[TASK START] resetNewMessages({})", id);
        try {
        	messageService.resetNewMessages(id);
            Instant end = Instant.now();
            long duration = Duration.between(start, end).toSeconds();
            logger.info("[TASK END] resetNewMessages({}) - {} s ", id, duration);
        } catch (Exception e) {
            logger.error("[TASK ERROR] resetNewMessages({}) ", id, e);
        }
	}
	
	// Updates messages of the order identified by id
	@PostMapping("updateMessages/{id}")
	public void updateMessages(@PathVariable String id) {
		Instant start = Instant.now();
        logger.info("[TASK START] updateMessages({})", id);
        try {
        	messageService.updateMessages(orderService.getOrder(id));
            Instant end = Instant.now();
            long duration = Duration.between(start, end).toSeconds();
            logger.info("[TASK END] updateMessages({}) - {} s ", id, duration);
        } catch (Exception e) {
            logger.error("[TASK ERROR] updateMessages({}) ", id, e);
        }
	}
	
	// Send message to the order identified by id
	@PostMapping("sendMessage/{id}")
	public void sendMessage(@PathVariable String id, @RequestBody MessageRequest request) {
	    Instant start = Instant.now();
        logger.info("[TASK START] sendMessage({}, {})", id, request.getMessage());
        try {
        	messageService.sendMessage(id, request.getMessage());
            Instant end = Instant.now();
            long duration = Duration.between(start, end).toSeconds();
            logger.info("[TASK END] sendMessage({}, {}) - {} s ", id, request.getMessage(), duration);
        } catch (Exception e) {
            logger.error("[TASK ERROR] sendMessage({}, {}) ", id, request.getMessage(), e);
        }
	}
}
