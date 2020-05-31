package com.ppmo.devopstutorial;

import java.math.BigDecimal;
import org.slf4j.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DevopsTutorialApplication {
	private static Logger logger = LoggerFactory.getLogger(DevopsTutorialApplication.class);

	public static void main(String[] args) {
		BigDecimal value = null;
		logger.info("My value {}", value.intValue());

		SpringApplication.run(DevopsTutorialApplication.class, args);
	}
}
