package com.absolics.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.absolics.vo.ParsingRuleVo;

@Service
public class PropertyManager {
	
	private static final PropertyManager instance = new PropertyManager();
	
	// join parsing data & mapping data
	private List<ParsingRuleVo> parsingRule;
	
	private List<ParsingRuleVo> mappingRule;
	
	@Value("${rule.sql.parsing}")
	private String inserParsingInspectDataSql;
	
	@Value("${rule.sql.mapping}")
	private String inserParsingInstrumentationDataSql;

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

	public String getInserParsingInspectDataSql() {
		return inserParsingInspectDataSql;
	}

	public String getInserParsingInstrumentationDataSql() {
		return inserParsingInstrumentationDataSql;
	}

	

	
}
