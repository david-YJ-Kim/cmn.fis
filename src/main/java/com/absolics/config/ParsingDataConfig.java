package com.absolics.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParsingDataConfig {
	
	@Value("${spring.datasource.url}")
	private String driveUrl;
	
	@Value("${spring.datasource.username}")
	private String userName;
	
	@Value("${spring.datasource.password}")
	private String passWord;
	
	@Value("${spring.datasource.driver-class-name}")
	private String driverClassName;
}
