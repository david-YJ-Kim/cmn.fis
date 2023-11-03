package com.absolics.storage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import com.absolics.vo.ParsingDataVo;

@Repository
public class ParsingDataRepository {
	private static final Logger log = LoggerFactory.getLogger(ParsingDataRepository.class);

	@Autowired
	private JdbcTemplate jdbctmplat;
	
	@Value("${rule.sql.parsing}")
	private String inserParsingInspectData = "INSERT INTO CN_FIS_EDM_TMP (	\r\n"
			+ "				SITE_ID,\r\n"
			+ "				PROD_DEF_ID,\r\n"
			+ "				PROC_DEF_ID,\r\n"
			+ "				PROC_SGMT_ID,\r\n"
			+ "				EQP_ID,\r\n"
			+ "				LOT_ID,\r\n"
			+ "				PROD_MTRL_ID,\r\n"
			+ "				SUB_PROD_MTRL_ID,\r\n"
			+ "				MTRL_FACE_CD,\r\n"
			+ "				INSP_REPT_CNT,\r\n"
			+ "				X_VAL,\r\n"
			+ "				Y_VAL,\r\n"
			+ "				GRD_ID,\r\n"
			+ "				DFCT_ID,\r\n"
			+ "				DFCT_X_VAL,\r\n"
			+ "				DFCT_Y_VAL,\r\n"
			+ "				INSP_DT,\r\n"
			+ "				IMG_FILE_NM,\r\n"
			+ "				REVIEW_IMG_FILE_NM,\r\n"
			+ "				INSP_FILE_NM,\r\n"
			+ "				ATTR_1,\r\n"
			+ "				ATTR_2,\r\n"
			+ "				ATTR_N,\r\n"
			+ "				CRT_DT,\r\n"
			+ "				PRCS_STATE\r\n"
			+ "			) VALUES ("
			+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,CURRENT_TIMESTEMP,?)";
	
	@Value("${rule.sql.mapping}")
	private String inserParsingInstrumentationData = "INSERT INTO CN_FIS_EDM_TMP	(\r\n"
			+ "				SITE_ID,\r\n"
			+ "				PROD_DEF_ID,\r\n"
			+ "				PROC_DEF_ID,\r\n"
			+ "				PROC_SGMT_ID,\r\n"
			+ "				EQP_ID,\r\n"
			+ "				LOT_ID,\r\n"
			+ "				PROD_MTRL_ID,\r\n"
			+ "				MTRL_FACE_CD,\r\n"
			+ "				INSP_REPT_CNT,\r\n"
			+ "				X_VAL,\r\n"
			+ "				Y_VAL,\r\n"
			+ "				Z_VAL,\r\n"
			+ "				DCITEM_ID,\r\n"
			+ "				RSLT_VAL,\r\n"
			+ "				IMG_FILE_NM,\r\n"
			+ "				INSP_FILE_NM,\r\n"
			+ "				ATTR_1,\r\n"
			+ "				ATTR_2,\r\n"
			+ "				ATTR_N,\r\n"
			+ "				CRT_DT\r\n"
			+ "				PRCS_STATE\r\n"
			+ "			) VALUES ("
			+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,CURRENT_TIMESTEMP, ?)";
	
	
	
//	public JSONObject insertParsingData(String fileType,List<ParsingDataVo> insertDatas) {
//		
//		TransactionStatus status = this.batchInsert(fileType, insertDatas);
//	
//		
//		
//		JSONObject result = new JSONObject();
//		
//		return result;
//	}
	
	@Transactional(rollbackFor = Exception.class)
	public TransactionStatus insertParsingData(String fileType, List<ParsingDataVo> insertDatas) {
		TransactionStatus status = null;
		int[] ret;
		
		try {
			if (fileType.equals("검사")) {
//				ret = jdbctmplat.batchUpdate(inserParsingInspectData, insertDatas);
				jdbctmplat.batchUpdate(inserParsingInspectData,
						new BatchPreparedStatementSetter() {							
							@Override
							public void setValues(PreparedStatement ps, int i) throws SQLException {
								// TODO Auto-generated method stub
							
								ps.setString(1, insertDatas.get(i).getSiteId());
								ps.setString(2, insertDatas.get(i).getProdDefId());
								ps.setString(3, insertDatas.get(i).getProcDefId());
								ps.setString(4, insertDatas.get(i).getProcSgmtId());
								ps.setString(5, insertDatas.get(i).getEqpId());
								ps.setString(6, insertDatas.get(i).getLotId());
								ps.setString(7, insertDatas.get(i).getProdMtrlId());
								ps.setString(8, insertDatas.get(i).getSubProdMtrlId());
								ps.setString(9, insertDatas.get(i).getMtrlFaceCd());
								ps.setString(10, insertDatas.get(i).getInspReptCnt());
								ps.setString(11, insertDatas.get(i).getxValue());
								ps.setString(12, insertDatas.get(i).getyValue());
								ps.setString(13, insertDatas.get(i).getGrdId());
								ps.setString(14, insertDatas.get(i).getDfctId());
								ps.setString(15, insertDatas.get(i).getDfctXvalue());
								ps.setString(16, insertDatas.get(i).getDfctYvalue());
								ps.setString(17, insertDatas.get(i).getInspDt());
								ps.setString(18, insertDatas.get(i).getImgFileName());
								ps.setString(19, insertDatas.get(i).getReviewImgFileName());
								ps.setString(20, insertDatas.get(i).getInspFileName());
								ps.setString(21, insertDatas.get(i).getAttr1());
								ps.setString(22, insertDatas.get(i).getAttr2());
								ps.setString(23, insertDatas.get(i).getAttrN());
								ps.setString(25, insertDatas.get(i).getPrcsState());
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
				jdbctmplat.batchUpdate(inserParsingInstrumentationData,
						new BatchPreparedStatementSetter() {							
							@Override
							public void setValues(PreparedStatement ps, int i) throws SQLException {
								// TODO Auto-generated method stub
							
								ps.setString(1, insertDatas.get(i).getSiteId());
								ps.setString(2, insertDatas.get(i).getProdDefId());
								ps.setString(3, insertDatas.get(i).getProcDefId());
								ps.setString(4, insertDatas.get(i).getProcSgmtId());
								ps.setString(5, insertDatas.get(i).getEqpId());
								ps.setString(6, insertDatas.get(i).getLotId());
								ps.setString(7, insertDatas.get(i).getProdMtrlId());
								ps.setString(8, insertDatas.get(i).getMtrlFaceCd());
								ps.setString(9, insertDatas.get(i).getInspReptCnt());
								ps.setString(10, insertDatas.get(i).getxValue());
								ps.setString(11, insertDatas.get(i).getyValue());
								ps.setString(12, insertDatas.get(i).getzValue());
								ps.setString(13, insertDatas.get(i).getDcitemId());
								ps.setString(14, insertDatas.get(i).getRsltVal());
								ps.setString(16, insertDatas.get(i).getPrcsState());
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
	
		return status;
	}
}
