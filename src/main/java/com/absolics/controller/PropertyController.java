package com.absolics.controller;

import java.util.List;

import com.absolics.service.PropertyManager;
import com.absolics.vo.ParsingRuleVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.absolics.repository.PasingRuleDataRepository;

@RestController
public class PropertyController {
	private static final Logger log = LoggerFactory.getLogger(PropertyController.class);

	/**
	 * FIS Property Initialize from restful url
	 * http://uri/init_psdt
	 * http://uri/init_mppdt 
	 * 
	 * TODO : init method to be here !! 
	 **/
	
	@Autowired
	private PasingRuleDataRepository psRule;
	
	/**
	 * Initialize Parsing Informations (SingleTone)
	 **/
	
	public boolean initParsingRuleData() {		
		// DB에서 테이블 값 그대로 row로만 읽어옴 
		PropertyManager.getPropertyManager().setMappingRule(psRule.initParsingMappingRule());
		PropertyManager.getPropertyManager().setParsingRule(psRule.initParsingRuleData());
		
		// 파싱 정보 에서 컬럼 정보, 로우정보 를 배열로 변환 ( 컬럼 순서 int[], 컬럼명 String[], 로우값 int[] )
		PropertyManager.getPropertyManager().setParsingRule(
				setMappingDataInfos(PropertyManager.getPropertyManager().getParsingRule())
				);	// 컬럼 정보 배열로
		
		// TODO : 현재 - psRuleMapper.initParsingFileRule() 한개의 쿼리에서 Join 및 파싱 하여 ListMap으로 return 
		// file parsing info & mapping info 를 1개의 dao list에 설정 해 놓는 것
		// 1개의 Query 로 해결 or 2번 select 후, java 에서 mearge 해야함. 선택.
		
		return true;
	}
	
	// 새로운 데이터를 sub에 loading 
	public boolean initStandby() {
		// DB에서 테이블 값 그대로 row로만 읽어옴 
				PropertyManager.getPropertyManager().setMappingRuleSub(psRule.initParsingMappingRule());
				PropertyManager.getPropertyManager().setParsingRuleSub(psRule.initParsingRuleData());
				
				// 파싱 정보 에서 컬럼 정보, 로우정보 를 배열로 변환 ( 컬럼 순서 int[], 컬럼명 String[], 로우값 int[] )
				PropertyManager.getPropertyManager().setParsingRuleSub(
						setMappingDataInfos(PropertyManager.getPropertyManager().getParsingRuleSub())
						);	// 컬럼 정보 배열로
		
		return true;
	}
	
	// 룰 교체
	public boolean initExchange() {
		// 현재 룰을 과거 룰로 이동
		PropertyManager.getPropertyManager().setParsingRulePast(
				PropertyManager.getPropertyManager().getParsingRule()
				);
		PropertyManager.getPropertyManager().setMappingRulePast(
				PropertyManager.getPropertyManager().getMappingRule()
				);
		
		// sub 에 준비된 신규 룰을 현재 룰로 이동
		PropertyManager.getPropertyManager().setParsingRule(
				PropertyManager.getPropertyManager().getParsingRuleSub()
				);
		PropertyManager.getPropertyManager().setMappingRule(
				PropertyManager.getPropertyManager().getMappingRuleSub()
				);
		
		return true;
	}

	
	// 룰 안에 있는  String 값 (B,D,F-H or 5,8-200) 으로 된 컬럼, 로우 정보를 배열로 치환 하여 탑재 
	private List<ParsingRuleVo> setMappingDataInfos(List<ParsingRuleVo> parsingRule) {
		
		for (int s = 0 ; s < parsingRule.size() ; s ++) {
			ParsingRuleVo pars = parsingRule.get(s);
			String[] headerInfo = null;
			String[] rowInfo = null;
			int[] headerInfoNum = null;
			int[] rowInfoNum = null;
			
			// 타이틀 값 배열로 변경 후 A B C  > 0 1 2 값으로 치환 어레이 설정 
			headerInfo = this.parsingArrayStringValues(pars.getParsingTitleInfo());
			headerInfoNum = this.columnSequence(headerInfo);
			
			// 파싱 룰 VO에 String List 탑재
			parsingRule.get(s).setParsingTitleInfo(headerInfo);
			parsingRule.get(s).setParsingTitles(headerInfoNum);
			
			// 파싱 로우 정보 array 로 반환			
			rowInfo = this.parsingArrayStringValues(pars.getParsingRowInfo());
			rowInfoNum = this.columnSequence(rowInfo);
			
			// 파싱 로우 데이터 VO에 탑재
			parsingRule.get(s).setParsingRowInfos(rowInfoNum);				
		}
		
		return parsingRule;
	}
	
	
	// , 와 - 로 입력된 값들을 하나의 배열로 변환 ex) B,D,F-H > B,D,F,G,H 
	private String[] parsingArrayStringValues(String info) {
		String[] ps = null;
				
		if (info.indexOf(",") != -1 && info.indexOf("-") != -1) {
			// header info 에 ',' 와  '-' 함께 있을 때
			ps = info.split(",");
			String[] split = null;
			for(int i = 0 ; i < ps.length ; i ++) {
				String newInfo = "";
				if( ps[i].indexOf("-") != -1 ) {
					// , ,의 값 사이에 있는   - 영역을 파싱 해옴
					split = this.parsingRangeInfos(ps[i]);
					
					// 파싱해 온 값을 해당 열에 , 어레이 String 으로 저장				
					for (String c : split) {
						newInfo += c.concat(",");
					}
				}
				info.replaceAll(ps[i], newInfo);
			}
			// 변경 저장된 값으로 다시 나눔
			ps = info.split(",");
			// haeder info를 다시 String으로
			
		} else if ( info.indexOf(",") != -1 ) {
			
			// header info ',' 를 배열로 나눔,				
			ps = info.split(",");
			
		} else if ( info.indexOf("-") != -1 ) {
			
			// header info 에 - 만 있을 경우				
			ps = this.parsingRangeInfos(info);				
			
		} else {
			log.error("## Not Informations param : "+info);
		}
				
		return ps;
	}
	
	// 문자열 파싱 컬럼 정보를 정수의 순차 정보로 변경하여 배열 변환 - 컬럼 정보 배열화 할 때에만
	private int[] columnSequence(String[] parsingTitles) {
		int[] colSeqs = new int[parsingTitles.length];
		
		for (int i=0 ; i < parsingTitles.length ; i++ ) {			
			colSeqs[i] = (int)parsingTitles[i].charAt(0) - (int)'A';
		}
		
		return colSeqs;
	}
	
	// 값이 a-g 일 때 a ~ g 까지의  값을 모두 갖는 문자 배열을 리턴함  - 컬럼정보, 로우정보 둘다 배열화 할 때
	private String[] parsingRangeInfos(String info) {
		// 'D-G' 값을 array[] = {'D', '-', 'G'} 로 변환
		char[] infos = info.toCharArray();
		
		// 값 확인 
		for(char a : infos) log.info("to char array : "+(int) a+"  "+ a);
		
		// 구간 값( array[0], array[1] )의 차수 구함
		int size = ( (int)infos[2] - (int)infos[0] ) +1;
		
		log.info("size : "+size);	// 배열 사이즈 확인
		
		String[] result = new String[size];
		
		for ( int i = 0 ; i < size ; i++) {
			result[i] = String.valueOf((char) (i+(int)infos[0]) );
			log.info( result[i] );
		}
		
		return result;
	}
	

	
//	@RequestMapping(value="/init_parseRuledata", method = RequestMethod.POST, produces = "application/text; charset=utf8")
//	public ResponseEntity<String> init_parseRefData(@RequestBody String msg) {		
//		if (this.initParsingRuleData()) {	// success initialize to parsing reference data.
//			
//			return ResponseEntity.status(HttpStatus.OK).body("Success initialize Reference Data for parsing");
//			
//		} else {	// failure initialize to parsing reference data.			
//			return ResponseEntity.badRequest().body("Failure initialize Reference Data!!");			
//		}		
//	}
	
}
