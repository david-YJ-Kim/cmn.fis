package com.absolics.storage;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.absolics.vo.ParsingRuleVo;

@Repository
public interface ParsingRuleStorage {
	
	/**
	 * Insert to Parsing Data
	 * @param map Map<String, Object>
	 * @return boolean
	 **/
	
	// 한개의 쿼리에서 parsing info, mapping info 한꺼번에 Join 및 파싱 하여 ListMap으로 return 
	List<ParsingRuleVo> initParsingRuleData();
	
	// parsing 기준 정보만 return
	List<ParsingRuleVo> initParsingFileRule();
	
	// insert mapping 정보만 return 
	List<ParsingRuleVo> initParsingMappingRule();
	
}
