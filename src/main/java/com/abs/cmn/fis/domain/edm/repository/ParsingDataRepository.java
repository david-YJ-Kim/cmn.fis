package com.abs.cmn.fis.domain.edm.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.abs.cmn.fis.config.FisPropertyManager;
import com.abs.cmn.fis.util.code.FisConstant;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import com.abs.cmn.fis.vo.ParsingRuleVo;

@Repository
@RequiredArgsConstructor
public class ParsingDataRepository {
	private static final Logger log = LoggerFactory.getLogger(ParsingDataRepository.class);

	private JdbcTemplate jdbctmplat;
		
	@Transactional(rollbackFor = Exception.class)
	public String batchInsert(String fileType, List<Map<String, String>> insertDatas,ParsingRuleVo rule) {
		TransactionStatus status = null;
		int[] ret;
		
		try {
			String[] keys = rule.getMappColumns();
			if (fileType.equals(FisConstant.Inpection.name())) {
//				ret = jdbctmplat.batchUpdate(inserParsingInspectData, insertDatas);
				jdbctmplat.batchUpdate(FisPropertyManager.getPropertyManager().getInserParsingInspectDataSql(),
						new BatchPreparedStatementSetter() {							
							@Override
							public void setValues(PreparedStatement ps, int i) throws SQLException {
								// TODO Auto-generated method stub
							
								for (int j = 0 ; j < insertDatas.size() ; j++ ) {
									ps.setString(i, insertDatas.get(i).get( keys[j] ));					
								}
							}
							
							@Override
							public int getBatchSize() {
								// TODO Auto-generated method stub 프로퍼티 파일에 설정 할 수 있도록
								return insertDatas.size();
							}
				});
//						insertDatas);
				
				// TODO : 입력 중 오류 방생 시 Rollback 은 신규 thread 생성 하여, 입력 중이던 파일의 모든 데이터를 삭제 처리 
			} else {
//				ret = jdbctmplat.batchUpdate(inserParsingInstrumentationData, insertDatas);
				jdbctmplat.batchUpdate(FisPropertyManager.getPropertyManager().getInserParsingInstrumentationDataSql(),
						new BatchPreparedStatementSetter() {							
							@Override
							public void setValues(PreparedStatement ps, int i) throws SQLException {
								// TODO Auto-generated method stub
								for (int j = 0 ; j < insertDatas.size() ; j++ ) {
									ps.setString(i, insertDatas.get(i).get( keys[j] ));					
								}
							}
							
							@Override
							public int getBatchSize() {
								// TODO Auto-generated method stub
								return insertDatas.size();
							}
				});
			}
			
		} catch (Exception e) {
			log.error("## ", e);
		}
	
		// transaction result 에서 상태 확인 후 keyvalue or rollback return		
		if (status.isCompleted()) {
			return status.toString();
		} else { 
			return FisConstant.ROLLBACK.name();
		}
	}
	
	public String rollback(String key) {
		int deletedRowNum = jdbctmplat.update(FisPropertyManager.getPropertyManager().getRollbackParsingData(), key);
		
		if ( deletedRowNum > 0)
			return FisConstant.SUCCESS.name();
		else
			return FisConstant.ROLLBACK_FAIL.name();
	}
}
