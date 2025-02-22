package dev.pablito.dots;

import dev.pablito.dots.aop.MeasurementAspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableScheduling
@EnableAspectJAutoProxy
public class DotsApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(DotsApplication.class, args);
	}

	@Bean
	public MeasurementAspect myAspect() {
		return new MeasurementAspect();
	}
}
