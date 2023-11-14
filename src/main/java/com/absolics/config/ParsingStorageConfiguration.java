package com.absolics.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import lombok.Getter;

@Getter
@Configuration
public class ParsingStorageConfiguration {
	
	Environment env;
	
	private static ParsingStorageConfiguration instance;
	
	@Value("${spring.datasource.url}")
	private String driveUrl;
	
	@Value("${spring.datasource.username}")
	private String userName;
	
	@Value("${spring.datasource.password}")
	private String passWord;
	
	@Value("${spring.datasource.driver-class-name}")
	private String driverClassName;

	public ParsingStorageConfiguration(Environment env) {
		this.env = env;
		instance = this;
	}
	
	public static ParsingStorageConfiguration getInstance() {
		return instance;
	}
	
}
