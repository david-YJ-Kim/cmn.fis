package com.abs.cmn.fis.util.vo;


import java.util.ArrayList;

import com.abs.cmn.fis.util.FisCommonUtil;
import com.abs.cmn.fis.util.code.FisFileType;

import lombok.Data;

@Data
public class ParseRuleVo {

    // parsing : OBJ_ID , mapping : REF_OBJ_ID
    private String objId;				    // Parsing Rule OBJ_ID
    private String eqpId;				    // 장비명

    /*
        파일 유형 검사 or 계측
        1. 검사: INSPECTION
        2. 계측: MEASURE
     */
    private FisFileType fileTyp;			// 파일 유형 검/계측
    
    /*
        파일 내, 데이터 시작하는 지점
     */
    private int headerStartValue;		        // 로우 시작 시점
    
    
    /*
        표준 파일에 대한 FIS 작업 완료 후, 이동할 디렉토리
     */
    private String fileMoveDirectoryValue;		    // parsing 처리 후 파일 이동 장소

    /*
        파일 내, 파싱 대상 컬럼 설정 (* CSV 파일 기준, A,B,C,D,E,... 컬럼 범위 선택 가능)
        1. 단일 컬럼        (A열)
        2. 범위 설정        (C열 ~ E열)
        3. 복합 범위        (A열과 C열 ~  E열)
        ex) A / C-E / A,C-E
        ※ 기준 정보 셋업 시, `parseClmIdValStrList` 객체명으로 컬럼 리스트 설정 (A,C,D,E)
        ※※ `parseClmIdValIntList` 컬럼 값을 기준으로 index 설정 (A: 0, B: 1, ...)
     */
    private String parsClmIdVal;
    /*
        파일 내, 파싱 대상 로우 설정 (파일마다 파싱 로우 범위 선택 가능)
        1. 단일 라인    (52번)
        2. 범위 선정    (52번 ~ 200번)
        3. 복합 범위    (50번과 52번 ~ 200번)
        4. 전체         (전체)
        ex) 52 / 52-200 / 50,52-200 / *

        ※※ 기준정보 셋업 시, `parseRowValList` 객체명에서 리스트로 관리
     */
    private String parseRowVal;


    /*
     * 파싱 컬럼 정보 (parsClmIdVal)
     */
    // PARS_CLM_ID_VAL 를 문자 배열로 파싱 및 변경
    private String[] parseClmIdValStrList;		// 파싱 컬럼 A,D,G,H,I,J,L

    // PARS_CLM_ID_VAL 의 문자 배열을 파싱 하기 위한 번호 값으로 변경
    private int[] parseClmIdValIntList;			// 컬럼 번호 0,3,6,7,8,9,11


    /*
     * 파싱 로우 정보 (parseRowVal)
     */
    // PARS_ROW_VAL 를 숫자 배열로 파싱 및 변경
    private int[] parseRowValList;		// 파싱 로우 41,48,49,50,...,199,200


    /*
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
