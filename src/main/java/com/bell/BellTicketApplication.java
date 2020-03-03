package com.bell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={
"com.bell.controller.ticket", "com.bell.ticket"})
public class BellTicketApplication {
	/*
	 * To check if app is up, try: http://localhost:8080/actuator/health
	 */

	public static void main(String[] args) {
		SpringApplication.run(BellTicketApplication.class, args);
	}

}
