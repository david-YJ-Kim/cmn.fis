package com.absolics.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.absolics.vo.ParsingRuleVo;

/**
 * 파일 파싱을 위한 기준 정보 객체
 * 
 * 객체는 3개를 진행 한다. init 할 때에는 
 * 1. Sub 객체에 읽어오고, 
 * 2. 새로운 룰 적용 하는 메소드 별도 작성\
 * 		: main -> past, sub -> main  정상 고체시 main과 sub은 동일 객체  
 **/

@Service
public class PropertyManager {
	
	private static final PropertyManager instance = new PropertyManager();
	
	// 파싱룰 데이터
	private List<ParsingRuleVo> parsingRule;
	
	// 매핑 룰 데이터
	private List<ParsingRuleVo> mappingRule;
		
	// 파싱룰 데이터
	private List<ParsingRuleVo> parsingRuleSub;
	
	// 매핑 룰 데이터
	private List<ParsingRuleVo> mappingRuleSub;

	// 파싱룰 데이터
	private List<ParsingRuleVo> parsingRulePast;
	
	// 매핑 룰 데이터
	private List<ParsingRuleVo> mappingRulePast;

	
	
	@Value("${rule.sql.parsing}")
	private String inserParsingInspectDataSql;
	
	@Value("${rule.sql.mapping}")
	private String inserParsingInstrumentationDataSql;
	
	@Value("${rule.sql.rollback}")
	private String rollbackParsingData;

	private PropertyManager() {}
	
	public static PropertyManager getPropertyManager() {
		return instance;
	}

	public List<ParsingRuleVo> getParsingRule() {
		return this.parsingRule;
	}

	public void setParsingRule(List<ParsingRuleVo> parsingRule) {
		this.parsingRule = parsingRule;
	}
	
	public List<ParsingRuleVo> getMappingRule() {
		return this.mappingRule;
	}
	
	public void setMappingRule(List<ParsingRuleVo> mappingRule) {
		this.mappingRule = mappingRule;
	}

	public List<ParsingRuleVo> getParsingRuleSub() {
		return parsingRuleSub;
	}

	public void setParsingRuleSub(List<ParsingRuleVo> parsingRuleSub) {
		this.parsingRuleSub = parsingRuleSub;
	}

	public List<ParsingRuleVo> getMappingRuleSub() {
		return mappingRuleSub;
	}

	public void setMappingRuleSub(List<ParsingRuleVo> mappingRuleSub) {
		this.mappingRuleSub = mappingRuleSub;
	}

	public List<ParsingRuleVo> getParsingRulePast() {
		return parsingRulePast;
	}

	public void setParsingRulePast(List<ParsingRuleVo> parsingRulePast) {
		this.parsingRulePast = parsingRulePast;
	}

	public List<ParsingRuleVo> getMappingRulePast() {
		return mappingRulePast;
	}

	public void setMappingRulePast(List<ParsingRuleVo> mappingRulePast) {
		this.mappingRulePast = mappingRulePast;
	}

	public String getInserParsingInspectDataSql() {
		return inserParsingInspectDataSql;
	}

	public String getInserParsingInstrumentationDataSql() {
		return inserParsingInstrumentationDataSql;
	}

	public String getRollbackParsingData() {
		return rollbackParsingData;
	}

	public void setRollbackParsingData(String rollbackParsingData) {
		this.rollbackParsingData = rollbackParsingData;
	}
	
}
