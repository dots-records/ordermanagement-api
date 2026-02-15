package dev.pablito.dots.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import dev.pablito.dots.aop.Timed;
import dev.pablito.dots.services.OrderService;

@Component
@CrossOrigin
public class OrderScheduler {

	@Autowired
	private OrderService orderService;

	private static final Logger logger = LoggerFactory.getLogger(OrderScheduler.class);

	@Timed
	@Scheduled(fixedRate = 60000)
	public void checkOrdersInDiscogs() {
		try {
			orderService.checkOrdersInDiscogs();
		} catch (Exception e) {
			logger.error("[SCHEDULED ERROR] checkOrdersInDiscogs() ", e);
		}
	}

	@Timed
	// @Scheduled(fixedRate = 360000)
	public void updateUpdatableDiscogsOrders() {
		try {
			orderService.updateUpdatableDiscogsOrders();
		} catch (Exception e) {
			logger.error("[SCHEDULED ERROR] updateUpdatableDiscogsOrders() ", e);
		}
	}

}
