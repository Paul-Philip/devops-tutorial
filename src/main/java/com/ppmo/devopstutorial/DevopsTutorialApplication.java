package com.ppmo.devopstutorial;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DevopsTutorialApplication {

	public static void main(String[] args) {
		AtomicInteger int1 = new AtomicInteger(1);
		AtomicInteger int2 = new AtomicInteger(2);

		if(int1.equals(int2)) {
			System.out.println("Get outta here, they are not eqaul");
		} else {
			System.out.println("1 and 2 are not equal no...");
		}

		SpringApplication.run(DevopsTutorialApplication.class, args);
	}

}
