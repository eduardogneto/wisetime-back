package com.wisetime.wisetime;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WisetimeApplication {

	public static void main(String[] args) {
		SpringApplication.run(WisetimeApplication.class, args);
	}

}
