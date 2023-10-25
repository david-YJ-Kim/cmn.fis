package com.absolics.mapper;

import java.util.List;
import java.util.Map;

public interface ParsingRuleMapper {
	
	/**
	 * Insert to Parsing Data
	 * @param map Map<String, Object>
	 * @return boolean
	 **/
	
	// 한개의 쿼리에서 parsing info, mapping info 한꺼번에 Join 및 파싱 하여 ListMap으로 return 
	List<Map<String, Object>> initParsingRuleData();
	
	// parsing 기준 정보만 return
	List<Map<String, Object>> initParsingFileRule();
	
	// insert mapping 정보만 return 
	List<Map<String, Object>> initParsingMappingRule();
	
}
