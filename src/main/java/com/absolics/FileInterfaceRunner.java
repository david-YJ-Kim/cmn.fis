package com.absolics;

import javax.sql.DataSource;

import com.absolics.config.SFTPProperty;
import com.absolics.config.SolaceConfiguration;
import com.absolics.controller.PropertyController;
import com.absolics.controller.SolacePublisher;
import com.absolics.controller.SolaceSubscriber;
import com.absolics.service.PropertyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import com.solacesystems.jcsmp.JCSMPException;

public class FileInterfaceRunner implements ApplicationListener<ApplicationStartedEvent>{
	private static final Logger log = LoggerFactory.getLogger(FisApplication.class);
	
	@Value("${property.type}")
	private String propertyType;

	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
		startFileInterface();
	}
	
	private void startFileInterface(){
		
		// MOS 로 메세지 송신 할 Publisher 실행
		try {
			
			SolacePublisher.getInstance().run();
			
		} catch (JCSMPException e) {
			
			// TODO Auto-generated catch block
			log.info("## Exception from Start to Publisher", e);
			
		}
		
		// File Info 수신 부  구동
		new Thread(new SolaceSubscriber()).run();
		
		PropertyController prop = new PropertyController();
		prop.initParsingRuleData();
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
    PropertyManager getPropertyManager() {
		return PropertyManager.getPropertyManager();
	}
	
	@Bean
	public JdbcTemplate jdbcTemplat(DataSource datasSource) {
		return new JdbcTemplate(datasSource);
	}
	
	@Bean
	public SolacePublisher getSolacePublisher() throws JCSMPException {
		return SolacePublisher.getInstance();
	}

}
