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

import com.absolics.value.FISValues;
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
//		try {
//			
//			result = filePs.toParsing(path, fileNm1, new List<ParsingRuleVo>());
//			if ( result != null )
//				log.info("## read success!!");
//			else
//				log.info("## fail read!!!");
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}
	
	
	// 
	public List<ParsingDataVo> toParsing(String fileType, String path, String fileName, List<ParsingRuleVo> list
							) throws IOException {
		
		// SFTP File 읽어오기
		File file = fileMng.getFile(path, fileName);
		
		// file header culumn row 이후 전체 파싱  , 컬럼 명이 몇번 row 인지 정보 받아와야 함. 
		List<Map<String, String>> prsDatas = this.parsCsvLine(file, 3);
		
		log.debug("return Data : "+ prsDatas.toString());
		
		List<ParsingDataVo> parsededList = this.replaceToReulst(fileType, prsDatas, list);
		
		return parsededList;
	}
	
	// csv file 전체 data 어읽 옴
	// TODO : 문의 : 어노테이션 확인해서 공유 하기
	@SuppressWarnings("finally")
	private List<Map<String, String>> parsCsvLine(File file, int header) throws IOException{
		
//		File file = null;
		BufferedReader br = null;
		
		List<Map<String, String>> parsDt = null;
//		List<ParsingDataVo> parsededList = null;
		
		String[] colNm = null;
		
		try {
			// 변경할 것 
			
			br = new BufferedReader(new FileReader(file));
			
			if ( file != null )	log.info("Check File exist - "+file.getPath()+file.getName());
			if ( br != null ) log.info("Check reader exist - "+br.toString());
			
			Charset.forName("UTF-8");	 // TODO 문의 : 확인 - 상위 단계에서 미리 설정해 놓을 수 있는지
			String line = "";
			
			parsDt = new ArrayList<Map<String, String>>();
			
			// Header Row info 필요
			int cnt = header;
			while ( (line = br.readLine()) != null ) {
				log.info(" line : " + line);
				
				String[] rows = null;
				Map<String, String> data = null;
				
				// column 정보  > 추후 colum info가 있는 line을  읽어야 함. 
				if ( cnt == header ) {
					
					colNm = line.split(",");
					data = new HashMap<String, String>();
					for (int i = 0 ; i < rows.length ; i++ )
						data.put(String.valueOf(i), colNm[i]);
					cnt ++;
					
				} else {
					
					rows = line.split(",");
					data = new HashMap<String, String>();
					
					for (int i = 0 ; i < rows.length ; i++ )
						data.put(colNm[i], rows[i]);
					
					parsDt.add(data);
					cnt ++;
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
	
	private List<ParsingDataVo> replaceToReulst(String fileType, List<Map<String, String>> parsDt, List<ParsingRuleVo> rule) {
		// SFPT Property 통해서 property 접근 property 는 SingleTone
		List<ParsingDataVo> inserDatas = new ArrayList<ParsingDataVo>();		
		ParsingDataVo row = null;
		
		// 검사 파일일 때
		if (fileType.equals(FISValues.Inpection.name())) {
		
			for (Map<String, String> psRow: parsDt) {
				for ( int i = 0 ; i < rule.size() ; i ++ ) {
					row = new ParsingDataVo();
					row.setSiteId( psRow.get(parsDt.get(i).get("0"))!=null?psRow.get(parsDt.get(i).get("0")):" " );
					row.setProdDefId( psRow.get(parsDt.get(i).get("1"))!=null?psRow.get(parsDt.get(i).get("1")):" " ) ;
					row.setProcDefId( psRow.get(parsDt.get(i).get("2"))!=null?psRow.get(parsDt.get(i).get("2")):" " ) ;
					row.setProcSgmtId( psRow.get(parsDt.get(i).get("3"))!=null?psRow.get(parsDt.get(i).get("3")):" " ) ;
					row.setEqpId( psRow.get(parsDt.get(i).get("4"))!=null?psRow.get(parsDt.get(i).get("4")):" " ) ;
					row.setLotId( psRow.get(parsDt.get(i).get("5"))!=null?psRow.get(parsDt.get(i).get("5")):" " ) ;
					row.setProdMtrlId( psRow.get(parsDt.get(i).get("6"))!=null?psRow.get(parsDt.get(i).get("6")):" " ) ;
					row.setSubProdMtrlId( psRow.get(parsDt.get(i).get("7"))!=null?psRow.get(parsDt.get(i).get("7")):" " ) ;
					row.setMtrlFaceCd( psRow.get(parsDt.get(i).get("8"))!=null?psRow.get(parsDt.get(i).get("8")):" " ) ;
					row.setInspReptCnt( psRow.get(parsDt.get(i).get("9"))!=null?psRow.get(parsDt.get(i).get("9")):" " ) ;
					row.setxValue( psRow.get(parsDt.get(i).get("10"))!=null?psRow.get(parsDt.get(i).get("10")):" " ) ;
					row.setyValue( psRow.get(parsDt.get(i).get("11"))!=null?psRow.get(parsDt.get(i).get("11")):" " ) ;
					row.setzValue( psRow.get(parsDt.get(i).get("12"))!=null?psRow.get(parsDt.get(i).get("12")):" " ) ;
					row.setLotId( psRow.get(parsDt.get(i).get("13"))!=null?psRow.get(parsDt.get(i).get("13")):" " ) ;
					row.setDcitemId( psRow.get(parsDt.get(i).get("14"))!=null?psRow.get(parsDt.get(i).get("14")):" " ) ;
					row.setRsltVal( psRow.get(parsDt.get(i).get("15"))!=null?psRow.get(parsDt.get(i).get("15")):" " ) ;
					row.setGrdId( psRow.get(parsDt.get(i).get("16"))!=null?psRow.get(parsDt.get(i).get("16")):" " ) ;
					row.setDfctId( psRow.get(parsDt.get(i).get("17"))!=null?psRow.get(parsDt.get(i).get("17")):" " ) ;
					row.setDfctXvalue( psRow.get(parsDt.get(i).get("18"))!=null?psRow.get(parsDt.get(i).get("18")):" " ) ;
					row.setDfctYvalue( psRow.get(parsDt.get(i).get("19"))!=null?psRow.get(parsDt.get(i).get("19")):" " ) ;
					row.setInspDt( psRow.get(parsDt.get(i).get("20"))!=null?psRow.get(parsDt.get(i).get("20")):" " ) ;
					row.setImgFileName( psRow.get(parsDt.get(i).get("21"))!=null?psRow.get(parsDt.get(i).get("21")):" " ) ;
					row.setReviewImgFileName( psRow.get(parsDt.get(i).get("22"))!=null?psRow.get(parsDt.get(i).get("22")):" " ) ;
					row.setInspFileName( psRow.get(parsDt.get(i).get("23"))!=null?psRow.get(parsDt.get(i).get("23")):" " ) ;
					row.setAttr1( psRow.get(parsDt.get(i).get("24"))!=null?psRow.get(parsDt.get(i).get("24")):" " ) ;
					row.setAttr2( psRow.get(parsDt.get(i).get("25"))!=null?psRow.get(parsDt.get(i).get("25")):" " ) ;
					row.setAttrN( psRow.get(parsDt.get(i).get("26"))!=null?psRow.get(parsDt.get(i).get("26")):" " ) ;			
				}
				inserDatas.add(row);
			}
		// 계측 파일일 때
		} else {
			for (Map<String, String> psRow: parsDt) {
				for ( int i = 0 ; i < rule.size() ; i ++ ) {
					row = new ParsingDataVo();
					row.setSiteId( psRow.get(parsDt.get(i).get("0"))!=null?psRow.get(parsDt.get(i).get("0")):" " );
					row.setProdDefId( psRow.get(parsDt.get(i).get("1"))!=null?psRow.get(parsDt.get(i).get("1")):" " ) ;
					row.setProcDefId( psRow.get(parsDt.get(i).get("2"))!=null?psRow.get(parsDt.get(i).get("2")):" " ) ;
					row.setProcSgmtId( psRow.get(parsDt.get(i).get("3"))!=null?psRow.get(parsDt.get(i).get("3")):" " ) ;
					row.setEqpId( psRow.get(parsDt.get(i).get("4"))!=null?psRow.get(parsDt.get(i).get("4")):" " ) ;
					row.setLotId( psRow.get(parsDt.get(i).get("5"))!=null?psRow.get(parsDt.get(i).get("5")):" " ) ;
					row.setProdMtrlId( psRow.get(parsDt.get(i).get("6"))!=null?psRow.get(parsDt.get(i).get("6")):" " ) ;
					row.setSubProdMtrlId( psRow.get(parsDt.get(i).get("7"))!=null?psRow.get(parsDt.get(i).get("7")):" " ) ;
					row.setMtrlFaceCd( psRow.get(parsDt.get(i).get("8"))!=null?psRow.get(parsDt.get(i).get("8")):" " ) ;
					row.setInspReptCnt( psRow.get(parsDt.get(i).get("9"))!=null?psRow.get(parsDt.get(i).get("9")):" " ) ;
					row.setxValue( psRow.get(parsDt.get(i).get("10"))!=null?psRow.get(parsDt.get(i).get("10")):" " ) ;
					row.setyValue( psRow.get(parsDt.get(i).get("11"))!=null?psRow.get(parsDt.get(i).get("11")):" " ) ;
					row.setzValue( psRow.get(parsDt.get(i).get("12"))!=null?psRow.get(parsDt.get(i).get("12")):" " ) ;
					row.setLotId( psRow.get(parsDt.get(i).get("13"))!=null?psRow.get(parsDt.get(i).get("13")):" " ) ;
					row.setDcitemId( psRow.get(parsDt.get(i).get("14"))!=null?psRow.get(parsDt.get(i).get("14")):" " ) ;
					row.setRsltVal( psRow.get(parsDt.get(i).get("15"))!=null?psRow.get(parsDt.get(i).get("15")):" " ) ;
					row.setGrdId( psRow.get(parsDt.get(i).get("16"))!=null?psRow.get(parsDt.get(i).get("16")):" " ) ;
					row.setDfctId( psRow.get(parsDt.get(i).get("17"))!=null?psRow.get(parsDt.get(i).get("17")):" " ) ;
					row.setDfctXvalue( psRow.get(parsDt.get(i).get("18"))!=null?psRow.get(parsDt.get(i).get("18")):" " ) ;
					row.setDfctYvalue( psRow.get(parsDt.get(i).get("19"))!=null?psRow.get(parsDt.get(i).get("19")):" " ) ;
					row.setInspDt( psRow.get(parsDt.get(i).get("20"))!=null?psRow.get(parsDt.get(i).get("20")):" " ) ;
					row.setImgFileName( psRow.get(parsDt.get(i).get("21"))!=null?psRow.get(parsDt.get(i).get("21")):" " ) ;
					row.setReviewImgFileName( psRow.get(parsDt.get(i).get("22"))!=null?psRow.get(parsDt.get(i).get("22")):" " ) ;
					row.setInspFileName( psRow.get(parsDt.get(i).get("23"))!=null?psRow.get(parsDt.get(i).get("23")):" " ) ;
					row.setAttr1( psRow.get(parsDt.get(i).get("24"))!=null?psRow.get(parsDt.get(i).get("24")):" " ) ;
					row.setAttr2( psRow.get(parsDt.get(i).get("25"))!=null?psRow.get(parsDt.get(i).get("25")):" " ) ;
					row.setAttrN( psRow.get(parsDt.get(i).get("26"))!=null?psRow.get(parsDt.get(i).get("26")):" " ) ;			
				}
				inserDatas.add(row);
			}			
		}
		
		return inserDatas;
	}
	
}
