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


	// TODO : 전반 적인 알고리즘은 여기서 수행한다. 
	/**
	 * path 정보를 JsonOBJ로 받아서 추출 
	 * 추후 파일 유형 및 타입 정보를 받을 수 있기 위해 JSONObj 로 사용, 필요하다면 Param 변경 가능
	 **/
	public void fileParsingStart(JSONObject msg) {
		
		JSONObject rstObj = null;
		
		try {
			
			if (msg.get("filePath") != null) {
				// FileManager 에서 get file 후 Parser로 던짐 parser는 resultList 를 repository 에 저장 된 결과 return 
				// 결과 값 Msg 로 송신 여기서.
				rstObj=  parser.toParsing(msg.getString("filePath"), msg.getString("fileName"), propMng.getParsingRule());
				
				// TODO : Repository 에서는 저장만, 하고 Service class 를 생성, 
				// 저장 후 File 이름을 key 로 BRS 에 송신 . 
				if ( rstObj != null ) {
					log.info("# @@ parsing success !! ");
					
					//TODO  회신 내용 Solace Sender 로 메세지 송신
					
				} else {
					rstObj = new JSONObject();
					//TODO jsonobject set to error msg : 파싱  요청 후 저장 회신 값이 없음 에 대한 내용 작성 또는 throws exception 발생!
				}
			} else {
				rstObj = new JSONObject();
				//TODO jsonobject set to error msg : filepathe 값이 없어 파일 파싱 불가능 에러 설정 후 회신
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("## FileReadExcutor - fileParsingStart - IOException : ", e);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			log.error("## FileReadExcutor - fileParsingStart - JSONException : ", e);
			
		}
						
	}
	
	
}
