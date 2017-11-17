package com.zeed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class ItunescardApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItunescardApplication.class, args);
	}
}
