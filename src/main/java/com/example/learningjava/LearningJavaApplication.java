package com.example.learningjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class LearningJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearningJavaApplication.class, args);
	}

}
