package com.absolics.service;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.absolics.vo.ParsingDataVo;
import com.absolics.vo.ParsingRuleVo;

@Service
public class FileParser {//extends JpaRepository<String, Object>{
	private static final Logger log = LoggerFactory.getLogger(FileParser.class);
	
	@Autowired
	private FileManager fileMng;
	
	// TODO : File 파싱만 해서 결과를 return 한다. 
	
	public static void main(String args[]) throws IOException{
		String path = "D:\\work-spaces\\work-space-3.9.11\\FileParsingInterface\\src\\main\\resources\\";
		String fileNm1 = "Absolics 계측 결과 파일 표준_20230918.csv";
		String fileNm2 = "Absolics 검사 결과 파일 표준_20230918.csv";
		
		FileParser filePs = new FileParser();
		
		JSONObject result = null;
		try {
			
			result = filePs.toParsing(path, fileNm1, null);
			if ( result != null )
				log.info("## read success!!");
			else
				log.info("## fail read!!!");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	// 
	public JSONObject toParsing(String path, String fileName, List<ParsingRuleVo> list
							) throws IOException {
		
		// call file parsing		
		List<Map<String, String>> prsDatas = this.parsCsvLine(path, fileName);
		
		log.debug("return Data : "+ prsDatas.toString());
		
		// TODO : 파싱 데이터를 익스큐터로 전달
		
		JSONObject rstObj = new JSONObject();
		// TODO :  
		
		return rstObj;
	}
	
	// csv file 전체 data 어읽 옴
	// TODO : 문의 : 어노테이션 확인해서 공유 하기
	@SuppressWarnings("finally")
	private List<Map<String, String>> parsCsvLine(String path, String fileName) throws IOException{
		
		File file = null;
		BufferedReader br = null;
		
		List<Map<String, String>> parsDt = null;
		List<ParsingDataVo> parsededList = null;
		
		String[] colNm = null;
		
		try {
			// Get file from nas SFTP method
			file = fileMng.getFile(path, fileName);
			// 변경할 것, 
			
			br = new BufferedReader(new FileReader(file));
			
			if ( file != null )	log.info("Check File exist - "+file.getPath()+file.getName());
			if ( br != null ) log.info("Check reader exist - "+br.toString());
			
			Charset.forName("UTF-8");	 // TODO 문의 : 확인 - 상위 단계에서 미리 설정해 놓을 수 있는지
			String line = "";
			
			parsDt = new ArrayList<Map<String, String>>();
			int cnt = 0;
			while ( (line = br.readLine()) != null ) {
				log.info(" line : " + line);
				
				String[] rows = null;
				
				if ( cnt == 0 ) {
					
					colNm = line.split(",");
					
				} else {
					
					rows = line.split(",");
					Map<String, String> data = new HashMap<String, String>();
					
					for (int i = 0 ; i < rows.length ; i++ )
						data.put(colNm[i], rows[i]);
					
					parsDt.add(data);
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
	
	private File getCVSFile(String fileInfo) {
		// SFPT Property 통해서 property 접근 property 는 SingleTone
		
		return null;
	}
	
}
