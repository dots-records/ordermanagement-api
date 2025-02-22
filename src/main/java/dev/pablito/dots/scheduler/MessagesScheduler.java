package dev.pablito.dots.scheduler;
import dev.pablito.dots.aop.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import dev.pablito.dots.entity.DatabaseOrder;
import dev.pablito.dots.services.MessageService;

@Component
public class MessagesScheduler {
	@Autowired
	private MessageService messageService;
	
	
	private static final Logger logger = LoggerFactory.getLogger(MessagesScheduler.class);

	// TODO: Change fixedRate to the number of orders * 1000
	// Updates messages of unarchived orders from database "Orders" every 5 min
	//@Scheduled(fixedRate = 300000) // 5 minutos
    @Timed
    public void updateUnarchivedMessages() {
        try {
        	messageService.updateUnarchivedMessages();
        } catch (Exception e) {
            logger.error("[SCHEDULED ERROR] updateUnarchivedMessages() ", e);
        }
    }
	
	// TODO: This has to be done only in the night
	// Updates messages of archived orders from database "Orders"
    @Timed
	public void updateArchivedMessages() {
        try {
        	messageService.updateArchivedMessages();
        } catch (Exception e) {
            logger.error("[SCHEDULED ERROR] updateArchivedMessages() ", e);
        }
	}

}
