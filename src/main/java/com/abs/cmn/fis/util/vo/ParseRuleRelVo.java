package com.abs.cmn.fis.util.vo;

import com.abs.cmn.fis.util.code.FisFileType;
import lombok.Builder;
import lombok.Data;

@Data
public class ParseRuleRelVo {

    private String objId;			// Parsing Rule OBJ_ID

    private String eqpId;				    // 장비명

    /*
        파일 유형 검사 or 계측
        1. 검사: INSPECTION
        2. 계측: MEASURE
     */
    private FisFileType fileTyp;			// 파일 유형 검/계측

    private String fileClmVal;		// FILE_CLM_NUM_VAL 파싱 컬럼 값

    private int fileClmNumIntVal;	// FILE_CLM_NUM_VAL 파싱 컬럼 의 숫자 값

    private String fileClmName;		// FILE_CLM_NM 파싱 컬럼명

    private String mpngClmNm;		// MPNG_CLM_NM 매핑 컬럼명

    private String clmDataTyp;		// 매핑 컬럼 데이터 타입

    @Builder
    public ParseRuleRelVo(String objId, String eqpId, FisFileType fileTyp, String fileClmVal, int fileClmNumIntVal, String fileClmName, String mpngClmNm, String clmDataTyp) {
        this.objId = objId;
        this.eqpId = eqpId;
        this.fileTyp = fileTyp;
        this.fileClmVal = fileClmVal;
        this.fileClmNumIntVal = fileClmNumIntVal;
        this.fileClmName = fileClmName;
        this.mpngClmNm = mpngClmNm;
        this.clmDataTyp = clmDataTyp;
    }
}
