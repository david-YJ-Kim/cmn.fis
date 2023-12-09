package com.abs.cmn.fis.util.vo;


import java.util.ArrayList;

import com.abs.cmn.fis.util.FisCommonUtil;
import com.abs.cmn.fis.util.code.FisFileType;

import lombok.Data;

@Data
public class ParseRuleVo {
	
	// parsing : OBJ_ID , mapping : REF_OBJ_ID
	private String objId;				// Parsing Rule OBJ_ID
	private String eqpId;				// 장비명
	private FisFileType fileTyp;			// 파일 유형 검/계측
	private int startHdrVal;		// 로우 시작 시점
	private String fileTgtPosnVal;		// parsing 처리 후 파일 이동 장소
	private String parsClmIdVal;	// 파싱 컬럼 정보 A,D,G-J,L   >>> parseClmIdValStrList >>>  parseClmIdValIntList
	private String parseRowVal;			// 파싱 로우 정보 41,48-200 or * >>> parseRowValList


	/**
	 * 파싱 컬럼 정보 (parsClmIdVal)
	 */
	// PARS_CLM_ID_VAL 를 문자 배열로 파싱 및 변경
	private String[] parseClmIdValStrList;		// 파싱 컬럼 A,D,G,H,I,J,L

	// PARS_CLM_ID_VAL 의 문자 배열을 파싱 하기 위한 번호 값으로 변경
	private int[] parseClmIdValIntList;			// 컬럼 번호 0,3,6,7,8,9,11


	/**
	 * 파싱 로우 정보 (parseRowVal)
	 */
	// PARS_ROW_VAL 를 숫자 배열로 파싱 및 변경
	private int[] parseRowValList;		// 파싱 로우 41,48,49,50,...,199,200


	/**
	 * REALTION 읽으면서 SET하는 항목들
	 */
	private String[] mpngClmStrList;  // RELATION 테이블에 등록된 타켓 컬럼 리스트
	
	private ArrayList<Integer> numberDataTypList;		// mpngClmnStrList에서 데이터 타입이 NUMBER인 컬럼의 인덱스
	
	private ArrayList<Integer> timeStampDataTypList;	// mpngClmnStrList에서 데이터 타입이 TIMESTAMP인 컬럼의 인덱스
	
	private String queryInsertBatch;	// 인서트 배치 쿼리 작성

	private ArrayList<ParseRuleRelVo> relationVoList;


	public ParseRuleVo(){
		if(relationVoList == null){
			relationVoList = new ArrayList<>();
		}
	}

	public void setParsClmIdVal(String parsClmIdVal) {
		this.parsClmIdVal = parsClmIdVal;

		this.parseClmIdValStrList = FisCommonUtil.parsingArrayStringValues(parsClmIdVal);
		if (parseClmIdValStrList.length > 1 ) {
			parseClmIdValIntList = FisCommonUtil.columnSequence(parseClmIdValStrList);
		}else{
			parseClmIdValIntList = new int[0];
		}
	}

	public void setParseRowVal(String parseRowVal) {
		this.parseRowVal = parseRowVal;

		String [] inputParsingRowStringArray = null;			// 파싱 RowInfo에 * 가 있을 경우와 그렇지 않은 경우를 분류 한다.
		if (!parseRowVal.equals("*")) {
			inputParsingRowStringArray = FisCommonUtil.parsingArrayStringValues(parseRowVal);
		}

		if (inputParsingRowStringArray!= null && inputParsingRowStringArray.length > 1 ) {
			this.parseRowValList = FisCommonUtil.setRowNumList(inputParsingRowStringArray);
		}else {
			this.parseRowValList = new int[0];
		}
	}



}
