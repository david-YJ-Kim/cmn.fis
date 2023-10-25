package com.absolics.service;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class FileReadExcutor {
	private static final Logger log = LoggerFactory.getLogger(FileReadExcutor.class);
	
	@Autowired
	private PropertyManager propMng;
	
	@Autowired
	private FileParser parser;

	public boolean fileParsingStart(JSONObject data) {
		try {
			if (data != null) {
				if ( parser.toParsing(data.getString("filePath"), data.getString("fileName")
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
