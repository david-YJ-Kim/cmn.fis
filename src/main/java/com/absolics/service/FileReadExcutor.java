package com.absolics.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import com.absolics.domain.work.model.CnFisWork;
import com.absolics.domain.work.service.CnFisWorkService;
import com.absolics.domain.work.vo.CnFisWorkSaveRequestVo;
import com.absolics.util.CommonDate;
import com.absolics.util.code.FisConstant;
import com.absolics.util.code.ProcessStateCode;
import com.absolics.value.FISValues;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.absolics.controller.SolacePublisher;
import com.absolics.vo.ParsingDataVo;
import com.solacesystems.jcsmp.JCSMPException;

@Service
public class FileReadExcutor {
	private static final Logger logger = LoggerFactory.getLogger(FileReadExcutor.class);
	
	@Autowired
	private PropertyManager propMng;
	
	@Autowired
	private FileParser parser;
	
	@Autowired
	private ParsingDataService repository;

	@Autowired
	private CnFisWorkService cnFisWorkService;
	

	// TODO : 전반 적인 알고리즘은 여기서 수행한다. 
	/**
	 * path 정보를 JsonOBJ로 받아서 추출 
	 * 추후 파일 유형 및 타입 정보를 받을 수 있기 위해 JSONObj 로 사용, 필요하다면 Param 변경 가능
	 **/
	public void fileParsingStart(JSONObject messageObj) throws JSONException {
		
		// 파싱 데이터 결과 값
		List<ParsingDataVo> parsedData = null;
		// 저장 후 File 이름 키값
		String res = null;
		JSONObject result = null;

		String fileName = messageObj.getString(FisConstant.fileName.name());
		String filePath = messageObj.getString(FisConstant.filePath.name());
		String fileType = messageObj.getString(FisConstant.fileType.name());
		String eqpId = messageObj.getString(FisConstant.eqpId.name());

		CnFisWork entity = this.cnFisWorkService.saveEntity(this.generateWorkVo("WFS", fileName, filePath, fileType, eqpId));
		logger.info("Work is generated. Entity: {}", entity.toString());

		try {
			
			if (filePath != null && fileName != null) {
				
				// 파싱 전 work id 생성 하는 로직 필요
				
				
				// FileManager 에서 get file 후 Parser로 던짐 parser는 resultList를  return
				parsedData =  parser.toParsing(fileType, filePath, fileName, propMng.getParsingRule() );
				
				// TODO : 파싱된 데이터 Repository 에서는 저장				
				if ( parsedData != null ) {
					
					//TODO  insert transaction 실행 
					// 파일 type과 work id 에 대한 값도 처리 해야함.
					result = repository.insertParsingData(messageObj.get("filePath").toString(), parsedData);
					
				} else {					
					
					//TODO Data 파싱 데이터 insert 실패 메세지 생성 후 JSONObject에 
					res = FISValues.ParsingFaile.name();
					result = new JSONObject();
					
				}

			} else {
				
				//TODO 파일 정보 관련 잘못 들어왔음을 key 값에 전달
				res = FISValues.InfoTribe.name();
				
				// key 값과 work-id 를 solace messageObj 로 JSONObject 생성
			}
			
			// 메세지 송신
			try {
				SolacePublisher.getInstance().sendMessage(result.toString());
			} catch (JCSMPException e) {
				// TODO Auto-generated catch block
				logger.info("## Failure Send Message to MOD", e);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("## FileReadExcutor - fileParsingStart - IOException : ", e);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			logger.error("## FileReadExcutor - fileParsingStart - JSONException : ", e);
			
		}
						
	}

	private CnFisWorkSaveRequestVo generateWorkVo(String reqSysNm, String fileName, String filePath, String fileType, String eqpId){
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		// TODO WORK 테이블 적재
		CnFisWorkSaveRequestVo vo = CnFisWorkSaveRequestVo.builder()
				.fileName(fileName)
				.filePath(filePath)
				.fileType(fileType)
				.eqpId(eqpId)
				.requestSystemName(reqSysNm) // TODO 우선 WFS
				.processState(ProcessStateCode.R.name())
				.createUserId(reqSysNm)
				.createDate(timestamp)
				.updateUserId(reqSysNm)
				.updateDate(timestamp)
				.build();
		return vo;
	}
	
	
}
