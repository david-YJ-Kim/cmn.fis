package com.abs.cmn.fis.config;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.abs.cmn.fis.vo.ParsingRuleVo;

/**
 * 파일 파싱을 위한 기준 정보 객체
 *
 * 객체는 3개를 진행 한다. init 할 때에는
 * 1. Sub 객체에 읽어오고,
 * 2. 새로운 룰 적용 하는 메소드 별도 작성\
 * 		: main -> past, sub -> main  정상 고체시 main과 sub은 동일 객체
 **/

@Component
@Getter
@Setter
public class FisPropertyManager {
	
	private static final FisPropertyManager instance = new FisPropertyManager();

	// TODO Need to be initialized
	// 파싱룰 데이터
	private List<ParsingRuleVo> parsingRule;

	// TODO Need to be initialized
	// 매핑 룰 데이터
	private List<ParsingRuleVo> mappingRule;


	@Value("${rule.sql.parsing}")
	private String insertParsingInspectionDataSql;
	
	@Value("${rule.sql.mapping}")
	private String insertParsingMeasurementDataSql;
	
	@Value("${rule.sql.rollback}")
	private String rollbackParsingData;

	private FisPropertyManager() {}
	
	public static FisPropertyManager getPropertyManager() {
		return instance;
	}

	public List<ParsingRuleVo> getParsingRule() {
		return this.parsingRule;
	}


}
