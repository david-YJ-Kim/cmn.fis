package com.absolics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.absolics.service.PropertyManager;

@RestController
public class PropertyController {

	/**
	 * FIS Property Initialize from restful url
	 * http://uri/init_psdt
	 * http://uri/init_mppdt 
	 **/
	
	@Autowired
	private PropertyManager propMng;
	
	
	@RequestMapping(value="/init_parseRuledata", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	public ResponseEntity<String> init_parseRefData(@RequestBody String msg) {
		
		if (propMng.initParsingRuleData()) {	// success initialize to parsing reference data.
			
			return ResponseEntity.status(HttpStatus.OK).body("Success initialize Reference Data for parsing");
			
		} else {	// failure initialize to parsing reference data.
			
			return ResponseEntity.badRequest().body("Failure initialize Reference Data!!");
			
		}
		
	}
	
}
