package com.abs.cmn.fis;

import javax.sql.DataSource;

import com.abs.cmn.fis.domain.edm.repository.PasingRuleDataRepository;
import com.abs.cmn.fis.domain.work.service.CnFisWorkService;
import com.abs.cmn.fis.domain.work.vo.CnFisWorkSaveRequestVo;
import com.abs.cmn.fis.intf.solace.SolacePublisher;
import com.abs.cmn.fis.intf.solace.SolaceSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import com.solacesystems.jcsmp.JCSMPException;

import java.sql.Timestamp;

public class FileInterfaceRunner implements ApplicationListener<ApplicationStartedEvent>{
	private static final Logger logger = LoggerFactory.getLogger(FileInterfaceRunner.class);
	
	@Value("${property.type}")
	private String propertyType;

	@Autowired
	private CnFisWorkService workService;

	@Autowired
	private PasingRuleDataRepository repository;


	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {

		logger.info(" FIS Application start.");
		this.repository
//		CnFisWorkSaveRequestVo vo = CnFisWorkSaveRequestVo.builder()
//				.updateUserId("WFS")
//				.fileType("A")
//				.fileName("FILEANME")
//				.filePath("FILEPATH")
//				.createUserId("DAVID")
//				.updateUserId("DAVID")
//				.createDate(new Timestamp(System.currentTimeMillis()))
//				.updateDate(new Timestamp(System.currentTimeMillis()))
//				.build();
//		this.workService.saveEntity(vo);

		this.startFileInterface();

	}
	
	private void startFileInterface(){
		
		// MOS 로 메세지 송신 할 Publisher 실행
		try {
			
			SolacePublisher.getInstance().run();
			
		} catch (JCSMPException e) {
			
			// TODO Auto-generated catch block
			logger.info("## Exception from Start to Publisher", e);
			
		}
		
		// File Info 수신 부  구동
//		new Thread(new SolaceSubscriber()).run();
		
//		PropertyController prop = new PropertyController();
//		prop.initParsingRuleData();
	}


	
//	@Bean
//	public CommandLineRunner runner() {
//		return (a) -> {
//			logger.info("########################################");
//			logger.info("[Deploy Properties Type] "+propertyType);
//			logger.info("########################################");
//		};
//	};
	
//	@Bean
//	public SolaceConfiguration getSessionConfiguration() {
//		return SolaceConfiguration.getSessionConfiguration();
//	}

//	@Bean
//	public SFTPProperty getsftpProperty() {
//		return SFTPProperty.getSftpProperty();
//	}
	
//	@Bean
//    PropertyManager getPropertyManager() {
//		return PropertyManager.getPropertyManager();
//	}
	
//	@Bean
//	public JdbcTemplate jdcbTemplat(DataSource datasSource) {
//		return new JdbcTemplate(datasSource);
//	}
	
//	@Bean
//	public SolacePublisher getSolacePublisher() throws JCSMPException {
//		return SolacePublisher.getInstance();
//	}


}
