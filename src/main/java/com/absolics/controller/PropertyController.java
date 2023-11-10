package com.absolics.controller;

import java.util.List;

import com.absolics.service.PropertyManager;
import com.absolics.vo.ParsingRuleVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
		// reload from database table the data of parsing reference
		PropertyManager.getPropertyManager().setMappingRule(psRule.initParsingMappingRule());
		PropertyManager.getPropertyManager().setParsingRule(psRule.initParsingRuleData());
				
		PropertyManager.getPropertyManager().setPsMppRule( 
				this.settingParseMappingData(
					PropertyManager.getPropertyManager().getParsingRule(),
					PropertyManager.getPropertyManager().getMappingRule()
				));
		
		// TODO : 현재 - psRuleMapper.initParsingFileRule() 한개의 쿼리에서 Join 및 파싱 하여 ListMap으로 return 
		// file parsing info & mapping info 를 1개의 dao list에 설정 해 놓는 것
		// 1개의 Query 로 해결 or 2번 select 후, java 에서 mearge 해야함. 선택.
		
		return true;
	}
	
	@RequestMapping(value="/init_parseRuledata", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	public ResponseEntity<String> init_parseRefData(@RequestBody String msg) {
		
		if (this.initParsingRuleData()) {	// success initialize to parsing reference data.
			
			return ResponseEntity.status(HttpStatus.OK).body("Success initialize Reference Data for parsing");
			
		} else {	// failure initialize to parsing reference data.
			
			return ResponseEntity.badRequest().body("Failure initialize Reference Data!!");
			
		}
		
	}
	
	private List<ParsingRuleVo> settingParseMappingData(List<ParsingRuleVo> parsing, List<ParsingRuleVo> mapping) {
		
		// culumn 파싱 데이터 배열로 갱신
		List<ParsingRuleVo> modifiedParsingRule = setParsingCulumnInfo(parsing);
		PropertyManager.getPropertyManager().setParsingRule(modifiedParsingRule);
		
		// 파싱 데이터 컬럼 정보 와 로우정보 mapping data에 저장
		for (int i = 0 ; i < mapping.size() ; i++) {
			for (ParsingRuleVo pars : modifiedParsingRule) {
				if ( mapping.get(i).getFileType().equals( pars.getFileType()) && mapping.get(i).getFileFormatType().equals( pars.getFileFormatType()) ) {
					// mapping 정보에 file parsing info 입히기, 그래서 그 줄만큼 돌면서 가져올 수 있게 기준 정보 갖고 있기.
					mapping.get(i).setParsingTitleInfo(modifiedParsingRule.get(i).getParsingTitleName());
					mapping.get(i).setParsingRowInfos(modifiedParsingRule.get(i).getParsingRowInfos());
				}
			}
		}
		
		return mapping; 
	}
	
	
	private List<ParsingRuleVo> setParsingCulumnInfo(List<ParsingRuleVo> parsingRule) {
		
		for (ParsingRuleVo pars : parsingRule) {
			String[] headerInfo = null;

			if (pars.getParsingTitleInfo().indexOf(",") != -1 && pars.getParsingTitleInfo().indexOf("-") != -1) {
				// header info 에 ',' 와  '-' 함께 있을 때
				headerInfo = pars.getParsingTitleInfo().split(",");
				String[] split = null;
				for(int i = 0 ; i < headerInfo.length ; i ++) {
					String newInfo = "";
					if( headerInfo[i].indexOf("-") != -1 ) {
						// , ,의 값 사이에 있는   - 영역을 파싱 해옴
						split = this.parsingRangeInfos(headerInfo[i]);
						
						// 파싱해 온 값을 해당 열에 , 어레이 String 으로 저장						
						for (String c : split) {
							newInfo += c.concat(",");
						}
					}
					pars.getParsingTitleInfo().replace(headerInfo[i], newInfo);						
				}
				// 변경 저장된 값으로 다시 나눔
				headerInfo = pars.getParsingTitleInfo().split(",");
				// haeder info를 다시 String으로
				
			} else if ( pars.getParsingTitleInfo().indexOf(",") != -1 ) {
				
				// header info ',' 를 배열로 나눔,				
				headerInfo = pars.getParsingTitleInfo().split(",");
				
			} else if ( pars.getParsingTitleInfo().indexOf("-") != -1 ) {
				
				// header info 에 - 만 있을 경우				
				headerInfo = this.parsingRangeInfos(pars.getParsingTitleInfo());				
				
			} else {
				log.error("## Not Informations to Parsing file header Info !! Check this rule  : " + pars.getFileType());
			}
				
		}
		
		return parsingRule;
	}
	
	// 값이 a-g 일 때 a ~ g 까지의  값을 모두 갖는 문자 배열을 리턴함
	private String[] parsingRangeInfos(String info) {
		char[] infos = info.toCharArray();
		
		// 'D-G' 와 같은 값을 10진수 연산으로 array 생성
		for(char a : infos)
			log.info("to char array : "+(int) a+"  "+ a);
		
		int size = ( (int)infos[2] - (int)infos[0] ) +1;
		
		log.info("size : "+size);
		
		String[] result = new String[size];
		
		for ( int i = 0 ; i < size ; i++) {
			result[i] = String.valueOf((char) (i+(int)infos[0]) );
			log.info( result[i] );
		}
		
		return result;
	}
	
}
