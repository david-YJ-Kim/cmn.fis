package com.absolics.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.absolics.dao.ParsingRuleDao;
import com.absolics.mapper.ParsingRuleMapper;

@Service
public class PropertyManager {
	
	private static final PropertyManager instance = new PropertyManager();
	
	// join parsing data & mapping data
	private List<Map<String,Object>> parsingRule;
	
	private List<Map<String,Object>> mappingRule;
	
	@Autowired
	ParsingRuleMapper psRuleMapper;

	private PropertyManager() {}
	
	public static PropertyManager getInstance() {
		return instance;
	}
	
	/**
	 * Initialize Parsing Informations (SingleTone)
	 **/
	public boolean initParsingRuleData() {
		// reload from database table the data of parsing reference
		setParsingRule(psRuleMapper.initParsingRuleData());
		setMappingRule(psRuleMapper.initParsingMappingRule());
		// TODO : 현재 - psRuleMapper.initParsingFileRule() 한개의 쿼리에서 Join 및 파싱 하여 ListMap으로 return 
		// file parsing info & mapping info 를 1개의 dao list에 설정 해 놓는 것
		// 1개의 Query 로 해결 or 2번 select 후, java 에서 mearge 해야함. 선택.
		
		return true;
	}
	
	public List<Map<String,Object>> getParsingRule() {
		return this.parsingRule;
	}

	private void setParsingRule(List<Map<String,Object>> parsingRule) {
		this.parsingRule = parsingRule;
	}
	
	public List<Map<String,Object>> getMappingRule() {
		return this.mappingRule;
	}
	
	private void setMappingRule(List<Map<String,Object>> mappingRule) {
		this.mappingRule = mappingRule;
	}
}
