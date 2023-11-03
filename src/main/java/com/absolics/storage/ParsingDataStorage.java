package com.absolics.storage;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;

import com.absolics.vo.ParsingDataVo;

@Repository
public interface ParsingDataStorage {
	
	/**
	 * Insert to Parsing Data
	 * @param map Map<String, Object>
	 * @return boolean
	 **/

	// parsing data 를 insert 한다. 	
	public TransactionStatus insertParsingData(String fileType, List<ParsingDataVo> insertDatas);
	
}
