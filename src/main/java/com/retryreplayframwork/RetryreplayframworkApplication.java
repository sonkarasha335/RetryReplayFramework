package com.retryreplayframwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RetryreplayframworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(RetryreplayframworkApplication.class, args);
	}

}
