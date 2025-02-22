package dev.pablito.dots.services;

import dev.pablito.dots.aop.Timed;
import dev.pablito.dots.entity.DatabaseNotification;
import dev.pablito.dots.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class NotificationService {
	
	@Autowired 
	private NotificationRepository notificationRepository;

	private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

	@Timed
	public Page<DatabaseNotification> getNotifications(int page, int size) throws IOException, InterruptedException {
		PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date"));
		return notificationRepository.findAll(pageable);
	}

}
