package com.abs.cmn.fis.domain.edm.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.abs.cmn.fis.vo.ParsingRuleVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PasingRuleDataRepository {
	private static final Logger logger = LoggerFactory.getLogger(PasingRuleDataRepository.class);

	@Autowired
	private JdbcTemplate jdbctmplat;
	
	
	private String selectParsingRuleSql = "SELECT\r\n" + 
			"			EQP_NM							as eqpName\r\n" + 
			"			,FILE_FM_TP						as fileFormatType\r\n" + 
			"			,FILE_TP						as fileType\r\n" + 
			"			,TG_FILE_MV_PT					as targetFileMovePath\r\n" + 
			"			,PRS_TTL_INFO					as parsingTitleInfo\r\n" + 
			"			,PRS_ROW_INFO					as parsingRowInfo\r\n" + 
			"		FROM\r\n" + 
			"			IF_PARSING_FILE_INFO";
	
	private String selectMappingRuleSql = "SELECT\r\n" + 
			"			EQP_NM							as eqpName\r\n" + 
			"			,FILE_FM_TP						as fileFormatType\r\n" + 
			"			,FILE_TP						as fileType			\r\n" + 
			"			,FILE_CLM_NM					as fileColumnName\r\n" + 
			"			,MPP_CLM_NM						as mappingColumnNamen\r\n" + 
			"		FROM\r\n" + 
			"			IF_PARSING_DATA_MAPPIN_INFO";
	
	
	public List<ParsingRuleVo> initParsingFileRule() {
		// TODO 파싱 정보 와 룰 정보를 머지해서 가져올 때
		
		List<ParsingRuleVo> parsingRules = null;		
		return parsingRules;
	}


	// CN_FIS_IF_PARSING_FILE_INFO
	public List<ParsingRuleVo> initParsingRuleData() {
		// TODO 파일 파싱 룰 가져오기
		List<ParsingRuleVo> parsingRules =  jdbctmplat.query(selectParsingRuleSql, new RowMapper<ParsingRuleVo>() {
			@Override
			public ParsingRuleVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				try {
					ParsingRuleVo row = new ParsingRuleVo();
					row.setParsingRowInfo(String.valueOf(rs.getInt(1)));
					row.setEqpName(rs.getString(2));
					row.setFileFormatType(rs.getString(3));
					row.setFileType(rs.getString(4));
					row.setTargetFileMovePath(rs.getString(5));
					row.setParsingRowInfo(rs.getString(6));
					row.setParsingRowInfo(rs.getString(7));
					return row;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
					logger.error("## SQLException - initParsingFileRule  mapRow : ", e);
				}
				return null;
				
			}
			
		});
		
		return parsingRules;
	}


	// CN_FIS_IF_PARSING_DATA_MAPPIN_INFO
	public List<ParsingRuleVo> initParsingMappingRule() {
		// TODO 데이터 맵핑 룰 가져오기
		List<ParsingRuleVo> parsingRules =  jdbctmplat.query(selectMappingRuleSql, new RowMapper<ParsingRuleVo>() {
			@Override
			public ParsingRuleVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				try {
					ParsingRuleVo row = new ParsingRuleVo();
					row.setParsingRowInfo(String.valueOf(rs.getInt(1)));
					row.setEqpName(rs.getString(2));
					row.setFileFormatType(rs.getString(3));
					row.setFileType(rs.getString(4));
					row.setTargetFileMovePath(rs.getString(5));
					row.setParsingRowInfo(rs.getString(6));
					row.setParsingRowInfo(rs.getString(7));
					return row;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
					logger.error("## SQLException - initParsingFileRule  mapRow : ", e);
				}
				return null;
				
			}
			
		});
		
		return parsingRules;
	}
}
