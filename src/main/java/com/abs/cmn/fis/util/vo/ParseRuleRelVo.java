package com.abs.cmn.fis.util.vo;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ParseRuleRelVo {
	
	private String objId;			// Parsing Rule OBJ_ID
	
	private String refObjId;		// Parsing Rule REF_OBJ_ID
	
	private String fileClmVal;		// FILE_CLM_NUM_VAL 파싱 컬럼 값
	
	private int fileClmNumIntVal;	// FILE_CLM_NUM_VAL 파싱 컬럼 의 숫자 값
	
	private String fileClmName;		// FILE_CLM_NM 파싱 컬럼명
	
	private String mpngClmNm;		// MPNG_CLM_NM 매핑 컬럼명
	
	private String clmDataTyp;		// 매핑 컬럼 데이터 타입

	@Builder
	public ParseRuleRelVo(String objId, String refObjId, String fileClmVal, int fileClmNumIntVal, String fileClmName, String mpngClmNm, String clmDataTyp) {
		this.objId = objId;
		this.refObjId = refObjId;
		this.fileClmVal = fileClmVal;
		this.fileClmNumIntVal = fileClmNumIntVal;
		this.fileClmName = fileClmName;
		this.mpngClmNm = mpngClmNm;
		this.clmDataTyp = clmDataTyp;
	}
}
