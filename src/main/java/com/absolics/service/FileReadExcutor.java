package com.absolics.service;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileReadExcutor {
	private static final Logger log = LoggerFactory.getLogger(FileReadExcutor.class);
	
	@Autowired
	private PropertyManager propMng;
	
	@Autowired
	private FileParser parser;

	
	/**
	 * path 정보를 JsonOBJ로 받아서 추출 
	 * 추후 파일 유형 및 타입 정보를 받을 수 있기 위해 JSONObj 로 사용, 필요하다면 Param 변경 가능
	 **/
	public boolean fileParsingStart(JSONObject path) {
		
		try {
			if (path != null) {
				if ( parser.toParsing(path.getString("filePath"), path.getString("fileName")
						, propMng.getParsingRule() ) ) {
					log.info("# @@ parsing success !! ");
				}
			} else {
				return false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("## FileReadExcutor - fileParsingStart - IOException : ", e);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			log.error("## FileReadExcutor - fileParsingStart - JSONException : ", e);
		}
		
		return true;
		
	}
	 
	/**
	 * file parsing method
	 **/
//	private void parsing(String filePath, String fileName, int sNum, int eNum) { 
//		HashMap<String, Object> inserMap = null;
//	
//		boolean result = parsingService.insertParsingData(inserMap);
//	}
	
}
