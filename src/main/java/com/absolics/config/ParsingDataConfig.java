package com.absolics.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParsingDataConfig {
	
	@Value("${spring.datasource.data}")
	private String driveName;
	
	
	private String userName;
	
	private String passWord;
	
	private String driverClassName;
}
