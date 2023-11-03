package com.absolics;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import com.absolics.config.SFTPProperty;
import com.absolics.config.SolaceConfiguration;
import com.absolics.controller.SolacePublisher;
import com.absolics.controller.SolaceSubscriber;
import com.absolics.service.PropertyManager;

public class FileInterfaceRunner implements ApplicationListener<ApplicationStartedEvent>{
	private static final Logger log = LoggerFactory.getLogger(FileInterfaceSystemApplication.class);
	
	@Value("${property.type}")
	private String propertyType;
	
	private PropertyManager propMng = PropertyManager.getInstance();
	
	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
		startFileInterface();
	}
	
	private void startFileInterface(){
		
		// MOS 로 메세지 송신 할 Publisher 실행
		SolacePublisher pub = new SolacePublisher();
		pub.run();
		
		// File Info 수신 부  구동
		new Thread(new SolaceSubscriber()).run();
		
		propMng.initParsingRuleData();
		
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
	
	@Bean
	public JdbcTemplate jdbcTemplat(DataSource datasSource) {
		return new JdbcTemplate(datasSource);
	}

}
