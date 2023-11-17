package com.abs.cmn.fis.message.move.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abs.cmn.fis.domain.edm.repository.ParsingDataRepository;
import com.abs.cmn.fis.domain.work.service.CnFisWorkService;
import com.abs.cmn.fis.message.move.FisFileMoveExecute;
import com.abs.cmn.fis.util.FisMessageList;
import com.abs.cmn.fis.util.service.FileManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FisFileMoveExecuteImpl implements FisFileMoveExecute {

	@Autowired
    private CnFisWorkService workService;
	
    @Autowired
    private FileManager fileManager;
    
    @Autowired
    private ParsingDataRepository parsingDataRepository;

    @Override
    public void init() {
    	log.info("## FisFileMoveExecuteImpl : init compleate.");
    }

    @Override
    public void execute(String fromPath, String fileName, String toPath, String workId, String type) throws Exception {
    	
    	// 1.워크 테이블 상태 변경  
    	
    	try {    		
    		// 파일 이동 및 삭제
    		if ( type != null && type.equals(FisMessageList.FIS_INTF_COMP) ) {
    		    			
    			log.info("########################### workId : "+workId);

    			// delete batch 실행
//    			String status = removeDatas(fromPath, fileName, toPath, workId);
    			long startTime = System.currentTimeMillis();
    			String status = this.parsingDataRepository.deleteBatch(workId, 1000);
    			log.info("ElapsedTime: {}ms", System.currentTimeMillis() - startTime);
    		    log.info(status);
    		    log.info("## workid : "+workId);
    			
    	        // 파일 이동 실행 
    	        if ( status != null && status.equals("complate"))	// complate code 등록 
    	        	fileManager.moveFile(fromPath, fileName, toPath); // return 변경 

    	        log.info("## FisFileMoveExecuteImpl, execute: Failed Move File. ");
    			log.info("## from filePath: {}", fromPath+fileName);
    			log.info("## to filePath: {}", toPath+fileName);
    			
			// 데이터 인서트 실패 시 삭제 진행
    		} else {
    			// delete batch 실행
//    			String status = removeDatas(fromPath, fileName, toPath, workId);
    			long startTime = System.currentTimeMillis();
    			String status = this.parsingDataRepository.deleteBatch(workId, 1000);
    			log.info("ElapsedTime: {}ms", System.currentTimeMillis() - startTime);
    		    log.info(status);
    		    log.info("## workid : "+workId);
    			log.info("## to delete workId: {}, fileName: {} status: {}", workId, fileName, status);
    		}

    	} catch (Exception e) {
    		log.error("## FisFileMoveExecuteImpl, execute: ", e);			
    	}
    	
    }
    
}

