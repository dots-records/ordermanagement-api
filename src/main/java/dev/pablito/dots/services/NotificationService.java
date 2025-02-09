package dev.pablito.dots.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import dev.pablito.dots.entity.DatabaseNotification;
import dev.pablito.dots.repository.NotificationRepository;

@Service
public class NotificationService {
	
	@Autowired 
	private NotificationRepository notificationRepository;
	
	public Page<DatabaseNotification> getNotifications(int page, int size) throws IOException, InterruptedException {
		PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date"));
		return notificationRepository.findAll(pageable);
	}

}
