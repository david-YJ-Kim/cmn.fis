package com.abs.cmn.fis.util.vo;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParseRuleVo {
	
	// parsing : OBJ_ID , mapping : REF_OBJ_ID
	private String objId;				// Praing Rule OBJ_ID
	
	// EQP_NM
	private String eqpName;				// 장비명
	
	// FILE_TYP
	private String fileType;			// 파일 유형 검/계측
	
	//FILE_TRGT_POSN_VAL
	private String fileTrgtPosnVal;		// parsing 처리 후 파일 이동 장소
	
	// PARS_CLM_ID_VAL
	private String parsingColmIdVal;	// 파싱 컬럼 정보 A,D,G-J,L
	
	// PARS_CLM_ID_VAL 를 문자 배열로 파싱 및 변경
	private String[] parseClmIdValStrList;		// 파싱 컬럼 A,D,G,H,I,J,L

	// PARS_CLM_ID_VAL 의 문자 배열을 파싱 하기 위한 번호 값으로 변경	
	private int[] parseClmIdValIntList;			// 컬럼 번호 0,3,6,7,8,9,11
	
	// PARS_ROW_VAL
	private String parseRowVal;			// 파싱 로우 정보 41,48-200 or *
	
	// PARS_ROW_VAL 를 숫자 배열로 파싱 및 변경
	private int[] parseRowValList;		// 파싱 로우 41,48,49,50,...,199,200

	private String[] mpngClmStrList;
	
	private int[] numberDtTypList;		// bachInsert의  Numberdatatype
	
	private int[] timeStmpDrTypList;
	
	private String queryInsertBatch;	// 인서트 배치 쿼리 작성
	
	public ParseRuleVo() {}
	
	@Builder
	public ParseRuleVo(String objId, String eqpName, String fileType,
			String fileTrgtPosnVal, String parsingColmIdVal, String[] parseClmIdValStrList, 
			int[] parseClmIdValIntList, String parseRowVal, int[] parseRowValList,
			String[] mpngClmStrList, int[] numberDtTypList, int[] timeStmpDrTypList, String queryInsertBatch) {
		this.objId=objId;
		this.eqpName = eqpName;
		this.fileType = fileType;
		this.fileTrgtPosnVal = fileTrgtPosnVal;
		this.parsingColmIdVal = parsingColmIdVal;
		this.parseClmIdValStrList = parseClmIdValStrList;
		this.parseClmIdValIntList = parseClmIdValIntList;
		this.parseRowVal = parseRowVal;
		this.parseRowValList = parseRowValList;
		this.mpngClmStrList = mpngClmStrList;
		this.queryInsertBatch = queryInsertBatch;
	}
}
