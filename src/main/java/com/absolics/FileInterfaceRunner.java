package com.absolics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

import com.absolics.config.SessionConfiguration;

public class FileInterfaceRunner implements ApplicationListener<ApplicationStartedEvent>{
	private static final Logger log = LoggerFactory.getLogger(FileInterfaceSystemApplication.class);
	
	@Value("${property.type}")
	private String propertyType;
	
	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
		
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
