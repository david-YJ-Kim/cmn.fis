package com.absolics.service;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.absolics.value.FISValues;
import com.absolics.vo.ParsingRuleVo;

@Service
public class FileParser {//extends JpaRepository<String, Object>{
	private static final Logger log = LoggerFactory.getLogger(FileParser.class);
	
	@Autowired
	private FileManager fileMng;
	
	// TODO : File 파싱만 해서 결과를 return 한다. 
	
	public static void main(String args[]) throws IOException{
//		String path = "D:\\work-space\\work-space-absolics\\FileParsingInterface\\src\\main\\resources\\";
		String path = "D:\\work-spaces\\work-space-3.9.11\\FileParsingInterface\\src\\main\\resources\\";
		String fileNm1 = "Absolics 검사 결과 파일 표준_20230918.csv";
		String fileNm2 = "Absolics 검사 결과 파일 표준_20230918.csv";
		
		FileParser filePs = new FileParser();
		
		JSONObject result = null;
		
		int[] colInfos = {1,4,5,7,8,9};
		int[] rowInfos = {41,42,43};		
//		log.info(": "+colInfos[3]+" , "+rowInfos[2]);
		
		ParsingRuleVo fileRule = new ParsingRuleVo(FISValues.Inpection.name(), colInfos, rowInfos);
		
		List<ParsingRuleVo> mappRule = new ArrayList<ParsingRuleVo>();
		ParsingRuleVo row = new ParsingRuleVo();
		
		row.setFileColumName("PROD_DEF_ID");
		row.setMappColumnName("CHANGE_PROD");
		row.setFileFormatType(FISValues.Inpection.name());
		mappRule.add(row);
		row.setFileColumName("EQP_ID");
		row.setMappColumnName("EQP");
		row.setFileFormatType(FISValues.Inpection.name());
		mappRule.add(row);
		
		List<Map<String,String>> rs = null;
		
		try {			
//			rs = filePs.parsCsvLines(path, fileNm1, fileRule, mappRule); // home test
			rs = filePs.toParsing(path, fileNm1, fileRule, mappRule); // 룰이 어레이여야 한다. 
			if ( rs != null ) {
				log.info("## read success!!" + rs.size());
				for (int i = 0 ; i < rs.size() ; i ++ )
					System.out.println(" ## "+ rs.get(i).toString() );
			} else {
				log.info("## fail read!!!");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		log.info("  A, 1 : "+(int)'A'+ ", "+(int)'1'+", "+String.valueOf((char)49));
	}
		
	public List<Map<String,String>> toParsing(String filePath, String fileName, ParsingRuleVo parsing 
											,List<ParsingRuleVo> mapping) throws IOException {
		// parsing Rule 객체를 찾아서 인자로 준다.
		File file = fileMng.getFile(filePath, fileName);
				
		return parsCsvLines(file, parsing, mapping);
	}
	
	private List<Map<String,String>> parsCsvLines(File file
												, ParsingRuleVo rule
												, List<ParsingRuleVo> mappRules) throws IOException {
		
		BufferedReader br = null;		
		List<Map<String, String>> parsDt = null;		
		String[] colNm = null;		
		
		
		try {
			
			br = new BufferedReader(new FileReader(file));		
			
			String line = "";
			
			// result 객체 생성
			parsDt = new ArrayList<Map<String, String>>();
			
			// 행 번호 컬럼 번호 정보 담아쓰기
			int[] rowsInfo = rule.getParsingRowInfos();
			int[] colsInfo = rule.getParsingTitles();
			
			// 파싱 데이터 그릇			
			String[] fileColunms = null;
			String[] splitRowDatas = null;
			
			int cnt=0;
			// 첫 줄 부터 로우 읽어옴.
			while ( (line = br.readLine()) != null ) {
				log.info("## :"+cnt+" line : " + line);
				
				// 시작 행 을 컬럼명 행으로 인지 파싱 저장
				if ( cnt == rule.getParsingRowInfos()[0]) {
					log.info("## in fifle 1st if :"+cnt+" line : " + line);
					fileColunms = line.split(",");
					cnt++;
				// 파싱 행번호가 어래이 안에 있다면, 파싱 저장
				} else if ( Arrays.binarySearch( rowsInfo, cnt ) >= 0 ){
					log.info("## in data pars 2nd if :"+cnt+" line : " + line);
					Map<String, String> data = new HashMap<String, String>();
					
					splitRowDatas = line.split(",");


					for (int i = 0 ; i < splitRowDatas.length ; i++ ) {
						
						// 컬럼 구분하여 맵에 저장
						if ( Arrays.binarySearch( colsInfo, i ) >= 0 ) {
							// 매핑 객체와 조회 하여 동일한 파일유형의 동일한 컬럼 값을 찾아와야 한다.
							String key = returnMappingColumnName(mappRules, rule, fileColunms[i]);
							data.put( key, splitRowDatas[i] );
						} else {
							log.info(" -- skip column num : "+i+", data : "+splitRowDatas[i]);
						}
					}
					parsDt.add(data);
					cnt++;
				// 그 외에 로우는 저장하지 않는다.
				} else {
					cnt++;
					log.info("skip Data : " + cnt+" , "+line);
					continue;
				}
			}
			
		} catch (FileNotFoundException  e) {
			
			// TODO: handle exception
			log.error("## File Not Found Exception : read - ", e);
			return null;
			
		} finally {
			
			if (br != null ) br.close();
			return parsDt;
			
		}
		
	}
	
	// 파일 컬럼 명과 파일타입 유형 키값 입력 시 매핑 컬럼명 반환 됨 - 파일 유형 고려 하여 전달 할 수 있도록 진행
	private String returnMappingColumnName(List<ParsingRuleVo> mapp, ParsingRuleVo rule, String fileColumn) {
		String key = null;
		
		for (ParsingRuleVo mappInfo : mapp) {
		
			if( mappInfo.getFileType().equals(rule.getFileType())
				 &&mappInfo.getFileFormatType().equals(rule.getFileFormatType())
				 &&mappInfo.getEqpName().equals(rule.getEqpName())
				 &&mappInfo.getFileColumName().equals(fileColumn) ) {
				
				key = mappInfo.getMappColumnName();
				
			} else {
				key = fileColumn;
			}
			
		}
		return key;
	}
	
}
