package com.abs.cmn.fis.domain.edm.repository;

import com.abs.cmn.fis.config.FisPropertyObject;
import com.abs.cmn.fis.util.FisCommonUtil;
import com.abs.cmn.fis.util.code.FisConstant;
import com.abs.cmn.fis.util.code.FisFileType;
import com.abs.cmn.fis.util.vo.ParseRuleVo;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.type.descriptor.sql.JdbcTypeFamilyInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.JDBCType;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class ParsingDataRepository {

	public String[] sqlColumList = null;

	@Autowired
	private JdbcTemplate jdbcTemplate;


	public String batchEntityInsert(String fileName, String workId, int headerOffSet, List<Map<String, String>> listMap, ParseRuleVo fileRule) {

		try {
			
			// TODO 파일 유형의 ObjId를 가져와야 한다. 위에 패스에서 확인 할 것.
			sqlColumList = fileRule.getMpngClmStrList();
			
			String query = fileRule.getQueryInsertBatch();	// TODO objId 로 전달 할 수 있도록 objId 관련 로직 전에 확인 하기
			
			int[] numberDataList = fileRule.getNumberDtTypList();
			int[] timeStmpDataList = fileRule.getTimeStmpDrTypList();
			
			if (numberDataList != null && timeStmpDataList != null )
				log.info("Insert For File type : {}", fileRule.getFileType() +" , "+numberDataList.length + ","+timeStmpDataList.length);
			 
			jdbcTemplate.setFetchSize(FisPropertyObject.getInstance().getBatchSize()); 
			
			jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					// TODO List<MAP> 변환 필요
					Map<String, String> map = listMap.get(i);
					ps.setString(1, FisCommonUtil.generateObjKey());
					ps.setString(2, fileRule.getFileTrgtPosnVal());
					ps.setString(3, fileName);					
					ps.setString(4, workId);
					ps.setInt(5, i+headerOffSet);
					
					for(int idx = 0; idx < sqlColumList.length; idx++){
						log.debug("Column:{}, Value: {}", sqlColumList[idx], map.getOrDefault(sqlColumList[idx], null));
						if ( FisCommonUtil.checkDataInList(numberDataList, idx) ) {
							ps.setInt(idx +6, Integer.valueOf( map.getOrDefault(sqlColumList[idx], null)) );
							log.info(idx+" , " +sqlColumList[idx]+", "+map.getOrDefault(sqlColumList[idx], null));
						} else if (FisCommonUtil.checkDataInList(timeStmpDataList, idx)){
							ps.setTimestamp(idx +6, Timestamp.valueOf(map.getOrDefault(sqlColumList[idx], null)) );
							log.info(idx+" , " +sqlColumList[idx]+", "+map.getOrDefault(sqlColumList[idx], null));
						} else {
							ps.setString(idx +6, map.getOrDefault(sqlColumList[idx], null));
							log.info(idx+" , " +sqlColumList[idx]+", "+map.getOrDefault(sqlColumList[idx], null));
						}
					}

				}

				@Override
				public int getBatchSize() {		// override 하는 이유를 찾아보자. - 이 안에 listmap size를 넣는게 맞는가? ,  
					// TODO Auto-generated method stub 프로퍼티 파일에 설정 할 수 있도록
					log.info(String.valueOf(listMap.size()));
					return listMap.size();
				}
			});
			log.info("Batch Complete. maxRows: {}", jdbcTemplate.getMaxRows());
			return "Complete";
			// TODO : 입력 중 오류 방생 시 Rollback 은 신규 thread 생성 하여, 입력 중이던 파일의 모든 데이터를 삭제 처리

		} catch (Exception vo) {
			vo.printStackTrace();
			log.error("## ", vo);
		}
		
		return workId;
	}

	
	public String deleteBatch(String fileType, String key, int batchSize) throws SQLException {
		
		int[] deletedRowNum = null;//jdbcTemplate.update(FisPropertyObject.getInstance().getRollbackQuery(), key);
		
		BatchPreparedStatementSetter batchSetter = new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, key);
				ps.setInt(2, batchSize);
			}
			
			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return batchSize;
			}
		};
		
		String deletBatch = FisPropertyObject.getInstance().getDeleteBatchTemplate();
		if (fileType.equals(FisFileType.INSP.name()))
			deletBatch.replace("TABLE_NAME", FisPropertyObject.getInstance().getTableNameInsp());
    	else
    		deletBatch.replace("TABLE_NAME", FisPropertyObject.getInstance().getTableNameMeas()); 
		
		deletedRowNum = jdbcTemplate.batchUpdate(deletBatch, batchSetter);
		
		log.debug("## deletedRowNum : "+Arrays.toString(deletedRowNum));
		
		if ( deletedRowNum.length < 0)
			return FisConstant.DELETE_FAIL.name();			
		else
			return FisConstant.DELETE_BATCH.name();
	}
	
}
