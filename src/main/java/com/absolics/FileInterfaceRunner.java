package com.absolics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

import com.absolics.config.SFTPProperty;
import com.absolics.config.SolaceConfiguration;
import com.absolics.controller.SolacePublisher;
import com.absolics.controller.SolaceSubscriber;

public class FileInterfaceRunner implements ApplicationListener<ApplicationStartedEvent>{
	private static final Logger log = LoggerFactory.getLogger(FileInterfaceSystemApplication.class);
	
	@Value("${property.type}")
	private String propertyType;
	
	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
		startFileInterface();
	}
	
	private void startFileInterface(){
		
		SolacePublisher pub = new SolacePublisher();
		pub.run();
		
		new Thread(new SolaceSubscriber()).run();
		
		
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
	public SolaceConfiguration getSessionConfiguration() {
		return SolaceConfiguration.getSessionConfiguration();
	}
	
	@Bean
	public SFTPProperty getsftpProperty() {
		return SFTPProperty.getSftpProperty();
	}

}
