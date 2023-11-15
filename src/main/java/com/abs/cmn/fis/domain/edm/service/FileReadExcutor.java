package com.abs.cmn.fis.domain.edm.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.abs.cmn.fis.config.FisPropertyManager;
import com.abs.cmn.fis.intf.solace.SolacePublisher;
import com.abs.cmn.fis.util.FileParser;
import com.abs.cmn.fis.util.code.FisConstant;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abs.cmn.fis.vo.ParsingRuleVo;
import com.solacesystems.jcsmp.JCSMPException;

@Service
public class FileReadExcutor {
	private static final Logger log = LoggerFactory.getLogger(FileReadExcutor.class);

	@Autowired
	private FisPropertyManager propMng;

	@Autowired
	private FileParser parser;

//	@Autowired
//	private ParsingDataService repository;


	// TODO : 전반 적인 알고리즘은 여기서 수행한다.
	/**
	 * path 정보를 JsonOBJ로 받아서 추출
	 * 추후 파일 유형 및 타입 정보를 받을 수 있기 위해 JSONObj 로 사용, 필요하다면 Param 변경 가능
	 **/
	public void fileParsingStart(JSONObject msg) {

		// 파싱 데이터 결과 값
		List<Map<String,String>> parsedData = null;
		// 저장 후 File 이름 키값
		String res = null;
		JSONObject result = null;
		ParsingRuleVo rule = null;

		try {

			if (msg.get("filePath") != null && msg.get("fileName") != null) {


				for (ParsingRuleVo vo : propMng.getParsingRule()) {
					if ( vo.getEqpName().equals(msg.getString("eqpId"))
							&& vo.getFileType().equals(msg.getString("fileType"))
							&& vo.getFileFormatType().equals(msg.getString("fileFormatType"))) {
						rule = vo;
						break;
					} else continue;
				}

				// 파싱 전 work id 생성 하는 로직 필요


				// FileManager 에서 get file 후 Parser로 던짐 parser는 resultList를  return
				try {
					parsedData =  parser.toParsing( msg.getString("filePath")
												, msg.getString("fileName")
												, rule
												, propMng.getMappingRule() );
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.error("@@ error occured file Parsing : " ,e);
				}

				// TODO : 파싱된 데이터 Repository 에서는 저장
				if ( parsedData != null ) {

					//TODO  insert transaction 실행
					// 파일 type과 work id 에 대한 값도 처리 해야함.
					result = repository.insertParsingDatas(msg.get("filePath").toString(), parsedData, rule);

				} else {

					//TODO Data 파싱 데이터 insert 실패 메세지 생성 후 JSONObject에
					res = FisConstant.ParsingFaile.name();
					result = new JSONObject();

				}

			} else {

				//TODO 파일 정보 관련 잘못 들어왔음을 key 값에 전달
				res = FisConstant.InfoTribe.name();

				// key 값과 work-id 를 solace msg 로 JSONObject 생성
			}

			// 메세지 송신
			try {
				SolacePublisher.getInstance().sendMessage(result.toString());
			} catch (JCSMPException e) {
				// TODO Auto-generated catch block
				log.info("## Failure Send Message to MOD", e);
			}

//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			log.error("## FileReadExcutor - fileParsingStart - IOException : ", e);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			log.error("## FileReadExcutor - fileParsingStart - JSONException : ", e);

		}

	}


}
