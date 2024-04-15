package com.abs.cmn.fis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
<<<<<<< HEAD
<<<<<<< HEAD
=======
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.task.TaskExecutor;
>>>>>>> failover
=======
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
>>>>>>> fa-threadJoin
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

//@EnableAsync
@SpringBootApplication()
public class FisApplication {

	public static void main(String[] args) {
		SpringApplication.run(FisApplication.class, args);
	}

<<<<<<< HEAD
//	@Bean
//	public TaskExecutor taskExecutor() {
//		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
//		taskExecutor.setCorePoolSize(2);
//		taskExecutor.setMaxPoolSize(2);
//		taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
//		taskExecutor.setAwaitTerminationSeconds(30);
//		taskExecutor.initialize();
//		return taskExecutor;
//	}
=======
>>>>>>> fa-threadJoin

}
