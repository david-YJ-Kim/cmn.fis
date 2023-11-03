package com.absolics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.absolics.repository.PasingRuleDataRepository;
import com.absolics.service.PropertyManager;

@RestController
public class PropertyController {

	/**
	 * FIS Property Initialize from restful url
	 * http://uri/init_psdt
	 * http://uri/init_mppdt 
	 * 
	 * TODO : init method to be here !! 
	 **/
	
	@Autowired
	PasingRuleDataRepository psRule;
	
	
	/**
	 * Initialize Parsing Informations (SingleTone)
	 **/
	
	public boolean initParsingRuleData() {
		// reload from database table the data of parsing reference
		PropertyManager.getPropertyManager().setMappingRule(psRule.initParsingMappingRule());
		PropertyManager.getPropertyManager().setParsingRule(psRule.initParsingRuleData());
				
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
	
}
