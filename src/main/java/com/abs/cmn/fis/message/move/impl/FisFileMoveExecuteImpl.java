package com.abs.cmn.fis.message.move.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abs.cmn.fis.domain.edm.repository.ParsingDataRepository;
import com.abs.cmn.fis.domain.work.service.CnFisWorkService;
import com.abs.cmn.fis.message.move.FisFileMoveExecute;
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
    public void execute(String fileType, String fileName, String filePath, String workId) throws Exception {
    	
    	// 1.워크 테이블 상태 변경  
    	
    	try {
    		
    		// 파일 이동 및 삭제
    		if ( fileManager.moveFile(fileType, fileName, "toFilePath") ) {
    		    			
    			// delete batch 실행
    			long startTime = System.currentTimeMillis();
    			String status = this.parsingDataRepository.deleteBatch(workId);
    			log.info("ElapsedTime: {}ms", System.currentTimeMillis() - startTime);
    	        log.info(status);
    			
    		} else {
    			
    			log.error("## FisFileMoveExecuteImpl, execute: Failed Move File. ");
    			log.error("## filePath {} fileName {}", filePath, fileName);
    			
    		}
    		    		
    		
    	} catch (Exception e) {
    		log.error("## FisFileMoveExecuteImpl, execute: ", e);			
    	}
    	
    }
    
    
}

