package com.abs.cmn.fisnew.domain.edm.repository;

import com.abs.cmn.fisnew.config.FisPropertyObject;
import com.abs.cmn.fisnew.domain.edm.model.CnFisEdcTmpVo;
import com.abs.cmn.fisnew.util.code.FisConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;

import javax.transaction.Transactional;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
@Slf4j
public class ParsingDataRepository {

	public String[] inspectionColumList = null;
	public String[] measurementColumList = null;

	@Autowired
	private JdbcTemplate jdbcTemplate;


	private void initializeColumList(){
		this.inspectionColumList = FisPropertyObject.getInstance().getInspectionColumList();
		this.measurementColumList = FisPropertyObject.getInstance().getMeasurementColumList();
		log.info("MeasurementQuery: {}, InspectionQuery: {}", Arrays.toString(this.measurementColumList), Arrays.toString(this.inspectionColumList));
	}


	public String batchEntityInsert(String workId, String fileType, List<Map<String, String>> listMap) {

		if(inspectionColumList == null || measurementColumList == null){
			this.initializeColumList();
		}

		try {
			if (fileType.equals(FisConstant.Inpection.name())) {
				log.info("Insert For File type : {}", FisConstant.Inpection.name());
				jdbcTemplate.setFetchSize(1000);

				jdbcTemplate.batchUpdate(
						FisPropertyObject.getInstance().getInsertParsingInspectionDataSql(),
						new BatchPreparedStatementSetter() {
							@Override
							public void setValues(PreparedStatement ps, int i) throws SQLException {


								// TODO List<MAP> 변환 필요
								Map<String, String> map = listMap.get(i);
								for(int idx = 0; idx < inspectionColumList.length; idx++){
									log.debug("Column:{}, Value: {}", inspectionColumList[idx], map.getOrDefault(inspectionColumList[idx], null));
									ps.setString(idx +1, map.getOrDefault(inspectionColumList[idx], null));
								}

							}

							@Override
							public int getBatchSize() {
								// TODO Auto-generated method stub 프로퍼티 파일에 설정 할 수 있도록
								log.info(String.valueOf(listMap.size()));
								return listMap.size();
							}
						});
				log.info("Batch Complete. maxRows: {}", jdbcTemplate.getMaxRows());
				return "Complete";
				// TODO : 입력 중 오류 방생 시 Rollback 은 신규 thread 생성 하여, 입력 중이던 파일의 모든 데이터를 삭제 처리


			} else {

				log.info(fileType);
			}

		} catch (Exception vo) {
			vo.printStackTrace();
			log.error("## ", vo);
		}


		return workId;
	}


//	@Transactional()
	public String batchInsert(String fileType, List<Map<String, String>> insertData, String[] columList) {
		TransactionStatus status = null;
		int[] ret;
		
		try {
			if (fileType.equals(FisConstant.Inpection.name())) {
				log.info("Insert For File type : {}", FisConstant.Inpection.name());

//				ret = jdbcTemplate.batchUpdate(inserParsingInspectData, insertData);
				jdbcTemplate.batchUpdate(
						FisPropertyObject.getInstance().getInsertParsingInspectionDataSql(),
						new BatchPreparedStatementSetter() {
							@Override
							public void setValues(PreparedStatement ps, int i) throws SQLException {

//								Map<String, String> map = insertData.get(i);
//								for(String col : columList){
//									ps.setString(i, map.get(col));
//								}
//								ps.setString(i, "파일네임");

								log.info("Index: {}", i);

								Map<String, String> map = insertData.get(i);

								ps.setString(1, "SITE_ID");
								ps.setString(2, "PROD_DEF_ID");
								ps.setString(3, "PROC_DEF_ID");
								ps.setString(4, "PROC_SGMT_ID");
								ps.setString(5, "EQP_ID");
								ps.setString(6, "LOT_ID");
								ps.setString(7, "PROD_MTRL_ID");
								ps.setString(8, "SUB_PROD_MTRL_ID");
								ps.setString(9, "MTRL_FACE_CD");
								ps.setString(10, "INSP_REPT_CNT");
								ps.setString(11, "X_VAL");
								ps.setString(12, "Y_VAL");
								ps.setString(13, "GRD_ID");
								ps.setString(14, "DFCT_ID");
								ps.setString(15, "DFCT_X_VAL");
								ps.setString(16, "DFCT_Y_VAL");
								ps.setString(17, "INSP_DT");
								ps.setString(18, "IMG_FILE_NM");
								ps.setString(19, "REVIEW_IMG_FILE_NM");
								ps.setString(20, "INSP_FILE_NM");
								ps.setString(21, "ATTR_1");
								ps.setString(22, "ATTR_2");
								ps.setString(23, "ATTR_N");
								ps.setString(24, "FILE_NM");







//								for(String value : map.values()){
//									ps.setString(i, value);
//								}
//								for (int j = 0 ; j < insertData.size() ; j++ ) {
//									ps.setString(i, insertData.get(i).get( keys[j] ));
//								}
							}
							
							@Override
							public int getBatchSize() {
								// TODO Auto-generated method stub 프로퍼티 파일에 설정 할 수 있도록
								return insertData.size();
							}
				});

				// TODO : 입력 중 오류 방생 시 Rollback 은 신규 thread 생성 하여, 입력 중이던 파일의 모든 데이터를 삭제 처리


			} else {
//				ret = jdbcTemplate.batchUpdate(inserParsingInstrumentationData, insertData);
				jdbcTemplate.batchUpdate(FisPropertyObject.getInstance().getInsertParsingMeasurementDataSql(),
						new BatchPreparedStatementSetter() {							
							@Override
							public void setValues(PreparedStatement ps, int i) throws SQLException {

								Map<String, String> map = insertData.get(i);
								for(String value : map.values()){
									ps.setString(i, value);
								}
							}
							
							@Override
							public int getBatchSize() {
								// TODO Auto-generated method stub
								return insertData.size();
							}
				});
			}
			
		} catch (Exception vo) {
			vo.printStackTrace();
			log.error("## ", vo);
		}
	
		// transaction result 에서 상태 확인 후 keyvalue or rollback return		
		if (status.isCompleted()) {
			return status.toString();
		} else { 
			return FisConstant.ROLLBACK.name();
		}
	}
	
//	public String rollback(String key) {
//		int deletedRowNum = jdbcTemplate.update(FisPropertyManager.getPropertyManager().getRollbackParsingData(), key);
//
//		if ( deletedRowNum > 0)
//			return FisConstant.SUCCESS.name();
//		else
//			return FisConstant.ROLLBACK_FAIL.name();
//	}
}
