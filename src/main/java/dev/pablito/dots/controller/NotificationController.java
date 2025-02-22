package dev.pablito.dots.controller;

import java.io.IOException;

import dev.pablito.dots.aop.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.pablito.dots.entity.DatabaseNotification;
import dev.pablito.dots.services.NotificationService;

@RestController
@CrossOrigin
@RequestMapping("/dots")
public class NotificationController {
	@Autowired
	private NotificationService notificationService;

	@Timed
	@GetMapping("/getNotifications/page={page}&size={size}")
	public ResponseEntity<Page<DatabaseNotification>>getNotifications(@PathVariable int page, @PathVariable int size) throws IOException, InterruptedException {
		return new ResponseEntity<Page<DatabaseNotification>>(notificationService.getNotifications(page, size), HttpStatus.OK);
	}
}
	
