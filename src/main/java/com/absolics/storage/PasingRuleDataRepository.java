package com.absolics.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.absolics.vo.ParsingRuleVo;

@Repository
public class PasingRuleDataRepository implements ParsingRuleStorage {
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
	
	
	// set parsing rull 
	@Override
	public List<ParsingRuleVo> initParsingFileRule() {
		
		List<ParsingRuleVo> parsingRules =  jdbctmplat.query(selectParsingRuleSql, new RowMapper<ParsingRuleVo>() {
			@Override
			public ParsingRuleVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				try {
					ParsingRuleVo row = new ParsingRuleVo();
					row.setPsRowinfo(String.valueOf(rs.getInt(0)));
					row.setEqpName(rs.getString(1));
					row.setFileFormatType(rs.getString(2));
					row.setFileType(rs.getString(3));
					row.setTargetFileMovePath(rs.getString(4));
					row.setParsingRowInfo(rs.getString(5));
					row.setParsingRowInfo(rs.getString(5));
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


	@Override
	public List<ParsingRuleVo> initParsingRuleData() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<ParsingRuleVo> initParsingMappingRule() {
		// TODO Auto-generated method stub
		return null;
	}
}
