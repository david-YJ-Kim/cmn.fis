package com.absolics.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.absolics.service.PropertyManager;
import com.absolics.vo.ParsingRuleVo;
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
	
	public List<ParsingRuleVo> initParsingFileRule() {
		// TODO 파싱 정보 와 룰 정보를 머지해서 가져올 때
		
		List<ParsingRuleVo> parsingRules = null;		
		return parsingRules;
	}


	public List<ParsingRuleVo> initParsingRuleData() {
		// TODO 파일 파싱 룰 가져오기
		List<ParsingRuleVo> parsingRules =  jdbctmplat.query(
				PropertyManager.getPropertyManager().getParsingRule().toString(), new RowMapper<ParsingRuleVo>() {
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


	public List<ParsingRuleVo> initParsingMappingRule() {
		// TODO 데이터 맵핑 룰 가져오기
		List<ParsingRuleVo> parsingRules =  jdbctmplat.query(PropertyManager.getPropertyManager().getMappingRule().toString()
					, new RowMapper<ParsingRuleVo>() {
			@Override
			public ParsingRuleVo mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				try {
					ParsingRuleVo row = new ParsingRuleVo();					
					row.setEqpName(rs.getString(1));
					row.setFileFormatType(rs.getString(2));
					row.setFileType(rs.getString(3));
					row.setParsingColNum(rs.getInt(4));
					row.setFileColumName(rs.getString(5));
					row.setMappColumnName(rs.getString(6));					
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
