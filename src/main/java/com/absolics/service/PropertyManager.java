package com.absolics.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.absolics.storage.PasingRuleDataRepository;
import com.absolics.vo.ParsingRuleVo;

@Service
public class PropertyManager {
	
	private static final PropertyManager instance = new PropertyManager();
	
	// join parsing data & mapping data
	private List<ParsingRuleVo> parsingRule;
	
	private List<ParsingRuleVo> mappingRule;
	
	@Autowired
	PasingRuleDataRepository psRule;

	private PropertyManager() {}
	
	public static PropertyManager getInstance() {
		return instance;
	}
	
	/**
	 * Initialize Parsing Informations (SingleTone)
	 **/
	public boolean initParsingRuleData() {
		// reload from database table the data of parsing reference
		setMappingRule(psRule.initParsingMappingRule());
		setParsingRule(psRule.initParsingRuleData());
				
		// TODO : 현재 - psRuleMapper.initParsingFileRule() 한개의 쿼리에서 Join 및 파싱 하여 ListMap으로 return 
		// file parsing info & mapping info 를 1개의 dao list에 설정 해 놓는 것
		// 1개의 Query 로 해결 or 2번 select 후, java 에서 mearge 해야함. 선택.
		
		return true;
	}
	
	public List<ParsingRuleVo> getParsingRule() {
		return this.parsingRule;
	}

	private void setParsingRule(List<ParsingRuleVo> parsingRule) {
		this.parsingRule = parsingRule;
	}
	
	public List<ParsingRuleVo> getMappingRule() {
		return this.mappingRule;
	}
	
	private void setMappingRule(List<ParsingRuleVo> mappingRule) {
		this.mappingRule = mappingRule;
	}
}
