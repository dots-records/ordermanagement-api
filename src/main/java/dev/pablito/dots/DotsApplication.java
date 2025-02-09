package dev.pablito.dots;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableScheduling
public class DotsApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(DotsApplication.class, args);
	}

}
