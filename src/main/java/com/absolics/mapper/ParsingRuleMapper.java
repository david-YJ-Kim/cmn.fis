package com.absolics.mapper;

import java.util.List;

public interface ParsingRuleMapper {
	
	/**
	 * Insert to Parsing Data
	 * @param map Map<String, Object>
	 * @return boolean
	 **/
	boolean inserParsingData(List<String> data);
	
}
