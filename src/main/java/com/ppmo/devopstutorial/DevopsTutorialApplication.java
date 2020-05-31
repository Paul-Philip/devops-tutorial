package com.ppmo.devopstutorial;

import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DevopsTutorialApplication {
	private static Logger logger = LoggerFactory.getLogger(DevopsTutorialApplication.class);
 
	public static void main(String[] args) {
		AtomicInteger int1 = new AtomicInteger(1);
		AtomicInteger int2 = new AtomicInteger(2);

		if(int1.equals(int2)) {
			logger.info("Get outta here, they are not eqaul");
		} else {
			logger.info("1 and 2 are not equal no...");
		}

		SpringApplication.run(DevopsTutorialApplication.class, args);
	}

}
