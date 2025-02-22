package dev.pablito.dots.controller;

import dev.pablito.dots.aop.Timed;
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
	@Timed
    @PostMapping("resetNewMessages/{id}")
	public void resetNewMessages(@PathVariable String id)  {
        try {
        	messageService.resetNewMessages(id);
        } catch (Exception e) {
            logger.error("[TASK ERROR] resetNewMessages({}) ", id, e);
        }
	}
	
	// Updates messages of the order identified by id
    @Timed
	@PostMapping("updateMessages/{id}")
	public void updateMessages(@PathVariable String id) {
        try {
        	messageService.updateMessages(orderService.getOrder(id));
        } catch (Exception e) {
            logger.error("[TASK ERROR] updateMessages({}) ", id, e);
        }
	}
	
	// Send message to the order identified by id
    @Timed
	@PostMapping("sendMessage/{id}")
	public void sendMessage(@PathVariable String id, @RequestBody MessageRequest request) {
        try {
        	messageService.sendMessage(id, request.getMessage());
        } catch (Exception e) {
            logger.error("[TASK ERROR] sendMessage({}, {}) ", id, request.getMessage(), e);
        }
	}
}
