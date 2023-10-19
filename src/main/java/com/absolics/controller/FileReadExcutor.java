package com.absolics.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.resource.HttpResource;

import com.absolics.storage.ParsingDataStorage;

@RestController
public class FileReadExcutor {
	
	@Autowired
	private ParsingDataStorage parsingService;
	
	/**
	 * initialize to Interface table datas from DB.
	 **/
	public void initializeIfDatas() {
		
		return ;
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
