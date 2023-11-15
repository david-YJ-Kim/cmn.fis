package com.abs.cmn.fis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "{com.abs}")
public class FisApplication {

	public static void main(String[] args) {
		SpringApplication.run(FisApplication.class, args);
	}

}
