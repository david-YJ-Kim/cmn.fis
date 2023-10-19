package com.absolics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.absolics.config.SessionConfiguration;

@SpringBootApplication
public class FileInterfaceSystemApplication {
	private static final Logger log = LoggerFactory.getLogger(FileInterfaceSystemApplication.class);

	@Value("${property.type}")
	private String propertyType;
	
	public static void main(String[] args) {
		SpringApplication.run(FileInterfaceSystemApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner() {
		return (a) -> {
			log.info("########################################");
			log.info("[Deploy Properties Type] "+propertyType);
			log.info("########################################");
		};
	};
	
	@Bean
	public SessionConfiguration getSessionConfiguration() {
		return SessionConfiguration.getSessionConfiguration();
	}
}
