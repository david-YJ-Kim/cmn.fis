package com.abs.cmn.fis.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.abs.cmn.fis.domain.rule.service.CnFisIfParseRuleRelService;
import com.abs.cmn.fis.domain.rule.service.CnFisIfParseRuleService;
import org.springframework.beans.factory.annotation.Autowired;

import com.abs.cmn.fis.config.FisPropertyObject;
import com.abs.cmn.fis.domain.rule.model.CnFisIfParseRule;
import com.abs.cmn.fis.domain.rule.model.CnFisIfParseRuleRel;
import com.abs.cmn.fis.util.code.FisFileType;
import com.abs.cmn.fis.util.vo.ParseRuleRelVo;
import com.abs.cmn.fis.util.vo.ParseRuleVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FisCommonUtil {

	public static void main(String[] args) {

		System.out.println(

				parsingRangeInfos("AD-AG")
		);
	}
	
	@Autowired
    private static CnFisIfParseRuleRelService cnFisIfParseRuleRelService;
	
	@Autowired
    private static CnFisIfParseRuleService cnFisIfParseRuleService;

    public static String generateClientName(String groupName, String siteName, String envType, String processSeq){
        return String.format("%s-%s-%s-", groupName, siteName, envType) + String.format("%04d", Integer.valueOf(processSeq) );
    }

    public static String generateObjKey(){
        String keyValueString = null;
        UUID uuid = UUID.randomUUID();
        String randomUuidString = uuid.toString();
        keyValueString = CommonDate.getCurrentDateTimeToString() + "-" + randomUuidString;
        return keyValueString;
    }

    public static List<ParseRuleRelVo> convertParseRuleRelVo(List<CnFisIfParseRuleRel> entities){

        ArrayList<ParseRuleRelVo> voList = new ArrayList<>();
        for(CnFisIfParseRuleRel e : entities){

        	ParseRuleRelVo vo = ParseRuleRelVo.builder()
            		.objId(String.valueOf(e.getRefObjId()))
            		.refObjId(String.valueOf(e.getRefObjId()))
            		.fileClmVal(e.getFileClmVal())
//            		.fileClmNumIntVal(changeClmTitlVal(e.getFileClmVal()))
					.fileClmNumIntVal(columnToIndex(e.getFileClmVal()))
                    .fileClmName(e.getFileClmNm())
                    .mpngClmNm(e.getMpngClmNm())
                    .clmDataTyp(e.getClmDataTyp())
                    .build();
            voList.add(vo);
            
            log.info("!! -- vo :: " + vo.toString());
        }
        return voList;

    }

    public static List<ParseRuleVo> convertParseRuleVo(List<CnFisIfParseRule> entities, List<ParseRuleRelVo> parseRuleRelVos){

        ArrayList<ParseRuleVo> voList = new ArrayList<>();
        for(CnFisIfParseRule e : entities){

            String inputParsingTitleInfo = e.getParsClmIdVal();		// 파싱 컬렁 아이디 값  - A,D,G-J,L
            String inputParsingRowInfo = e.getParsRowVal();			// 파싱 로우 정보 41,48-200 or *
            
            // 파싱 컬럼 문자 배열 - A,D,G,H,I,J,L 
            String [] parsingTitleStringArray = FisCommonUtil.parsingArrayStringValues(inputParsingTitleInfo);
            
            int[] parsingTitleIntArray = null;						// 파싱 컬럼 문자 배열을 숫자 배열로
            if (parsingTitleStringArray.length > 1 ) parsingTitleIntArray = FisCommonUtil.columnSequence(parsingTitleStringArray);
            else parsingTitleIntArray = new int[0];
            log.info(">> parsingTitleStringArray : " + parsingTitleStringArray.toString());
            log.info(">> parsingTitleIntArray : " + parsingTitleIntArray.toString());
            
            String [] inputParsingRowStringArray = null;			// 파싱 RowInfo에 * 가 있을 경우와 그렇지 않은 경우를 분류 한다. 
            if (inputParsingRowInfo.equals("*")) inputParsingRowStringArray = null; 
            else inputParsingRowStringArray = FisCommonUtil.parsingArrayStringValues(inputParsingRowInfo);
            
            int[] inputParsingRowIntArray = null;
            if (inputParsingRowStringArray.length > 1 ) inputParsingRowIntArray = FisCommonUtil.setRowNumList(inputParsingRowStringArray);
            else inputParsingRowIntArray = new int[0];
            
            log.info(">> inputParsingRowStringArray : " + inputParsingRowStringArray.toString());
            log.info(">> inputParsingRowIntArray : " + inputParsingRowIntArray.toString());
            
            String objId = e.getObjId();
            String query = FisCommonUtil.makeBatchInsertQuery(e.getFileTyp(), objId, parseRuleRelVos);
            String[] mpngClmList = FisCommonUtil.getMappingColums(String.valueOf(objId), parseRuleRelVos);
            int[] numberDtTypList = FisCommonUtil.getNumberDataTypeList(String.valueOf(objId), parseRuleRelVos);
            int[] timeStmpDrTypList = FisCommonUtil.getTimeStempTypeList(String.valueOf(objId), parseRuleRelVos);
            
            
            ParseRuleVo vo = ParseRuleVo.builder()
            		.objId(e.getObjId())
                    .eqpName(e.getEqpNm())
                    .fileFormatType(String.valueOf(e.getFileFmTyp()))
                    .fileType(e.getFileTyp())
                    .fileTrgtPosnVal(e.getFileTrgtPosnVal())
                    .parsingColmIdVal(e.getParsClmIdVal())
                    .parseClmIdValStrList(parsingTitleStringArray)	// 컬럼 정보 스트링 배열로
                    .parseClmIdValIntList(parsingTitleIntArray)		// 컬럼 정보 인트 배열로
                    .parseRowVal(e.getParsRowVal())
                    .parseRowValList(inputParsingRowIntArray)		// 로우 정보 인트 배열로
                    .mpngClmStrList(mpngClmList)					// SQL 기준 컬럼 비교 컬럼 리스트
                    .numberDtTypList(numberDtTypList)				// 
                    .timeStmpDrTypList(timeStmpDrTypList)
                    .queryInsertBatch(query)
                    .build();

            voList.add(vo);

        }
        return voList;

    }

    public static ParseRuleVo getParsingRule(String eqpId, String fileType, String fileFormatType){
        // TODO 기준 정보 순회
    	
        List<ParseRuleVo> rule =  FisPropertyObject.getInstance().getParsingRule();
        
        ParseRuleVo parsingRule = null;
        
        for (ParseRuleVo vo : rule) {
        	if ( vo.getEqpName().equals(eqpId) && vo.getFileType().equals(fileType) && vo.getFileFormatType().equals(fileFormatType) ) {
        		parsingRule = vo;
        		break;
        	} else {
        		parsingRule = null;	// 임의 설정 값 프로퍼티로?
        	}
        }

        return parsingRule;

    }

    public static String[] extractColumns(String query) {
        String regex = "\\((.*?)\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(query);

        if (matcher.find()) {
            String columnSection = matcher.group(1);
            String[] columns = columnSection.split("\\s*,\\s*");
            for (int i = 0; i < columns.length; i++) {
                columns[i] = columns[i].replaceAll("\\s", ""); // Remove all whitespaces
            }
            return columns;
        } else {
            return new String[0]; // Pattern not found, return an empty array
        }
    }

    // , 와 - 로 입력된 값들을 하나의 배열로 변환 ex) B,D,F-H > B,D,F,G,
    // TODO 전체선택 (*)도 여기서 대응
    private static String[] parsingArrayStringValues(String info) {
        String[] ps = null;

        if (info.indexOf(",") != -1 && info.indexOf("-") != -1) {
            // header info 에 ',' 와  '-' 함께 있을 때
            ps = info.split(",");
            String[] split = null;
            for(int i = 0 ; i < ps.length ; i ++) {
                String newInfo = "";
                if( ps[i].indexOf("-") != -1 ) {
                    // , ,의 값 사이에 있는   - 영역을 파싱 해옴
                    split = FisCommonUtil.parsingRangeInfos(ps[i]);

                    // 파싱해 온 값을 해당 열에 , 어레이 String 으로 저장
                    for (String c : split) {
                        newInfo += c.concat(",");
                    }
                }
                info.replaceAll(ps[i], newInfo);
            }
            // 변경 저장된 값으로 다시 나눔
            ps = info.split(",");
            // haeder info를 다시 String으로

        } else if ( info.indexOf(",") != -1 ) {

            // header info ',' 를 배열로 나눔,
            ps = info.split(",");

        } else if ( info.indexOf("-") != -1 ) {

            // header info 에 - 만 있을 경우
            ps = FisCommonUtil.parsingRangeInfos(info);

        } else {
            log.error("## Not Informations param : "+info);
        }

        return ps;
    }

    // 문자열 파싱 컬럼 정보를 정수의 순차 정보로 변경하여 배열 변환 - 컬럼 정보 배열화 할 때에만
    private static int[] columnSequence(String[] parsingTitles) {
        int[] colSeqs = new int[parsingTitles.length];

        for (int i=0 ; i < parsingTitles.length ; i++ ) {
            colSeqs[i] = (int)parsingTitles[i].charAt(0) - (int)'A';
        }

        return colSeqs;
    }
    
    private static int[] setRowNumList(String[] parsingRows) {
    	int[] rowList = new int[parsingRows.length];
    	
    	for ( int i = 0 ; i< parsingRows.length ; i++ )
    		rowList[i] = Integer.valueOf(parsingRows[i]);
    	
    	return rowList;
    }

    // 값이 a-g 일 때 a ~ g 까지의  값을 모두 갖는 문자 배열을 리턴함  - 컬럼정보, 로우정보 둘다 배열화 할 때
    private static String[] parsingRangeInfos(String info) {
        // 'AD-AG' 값을 array[] = {'D', '-', 'G'} 로 변환
        String[] infos = info.split("-");
		log.info(info);


        //char[] infos = info.toCharArray();

        // 값 확인
        //for(char a : infos) log.info("to char array : "+(int) a+"  "+ a);
        for (String s : infos) log.info("to char array : "+Integer.valueOf(s)+"  "+ s);

        // 구간 값( array[0], array[1] )의 차수 구함
        //int size = ( (int)infos[2] - (int)infos[0] ) +1;
        int size = ( Integer.valueOf(infos[1]) - Integer.valueOf(infos[0]) ) +1;

        log.info("size : "+size);	// 배열 사이즈 확인

        String[] result = new String[size];

        for ( int i = 0 ; i < size ; i++) {
            result[i] = String.valueOf((char) (i+Integer.valueOf(infos[0])) );
            log.info( result[i] );
        }

        return result;
    }
    
    public static boolean reloadBaseRuleData() {
    	try {
	    	// DB에서 기준 정보 읽어옴
	    	List<CnFisIfParseRuleRel> mappingInfoEntities = cnFisIfParseRuleRelService.getAllEntities();
	        List<ParseRuleRelVo> mappingInfoVos = FisCommonUtil.convertParseRuleRelVo(mappingInfoEntities);
	        FisPropertyObject.getInstance().setPrepMappingRule(mappingInfoVos);
	
	        List<CnFisIfParseRule> parsingInfoEntities = cnFisIfParseRuleService.getAllEntities();
	        List<ParseRuleVo> parsingInfoVos = FisCommonUtil.convertParseRuleVo(parsingInfoEntities, mappingInfoVos);
	        FisPropertyObject.getInstance().setPrepParsingRule(parsingInfoVos);
	        
	        // 현재 운영 룰 past에 저장 해 놓음
	        FisPropertyObject.getInstance().setPastMappingRule(
	        		FisPropertyObject.getInstance().getMappingRule()
	        		);
	        FisPropertyObject.getInstance().setPastParsingRule(
	        		FisPropertyObject.getInstance().getParsingRule()
	        		);
	        
	        return true;
        
    	} catch (Exception e) {
    		log.info("## FisCommonUtil, reloadBaseRuleData ", e);
    		return false;
    	}       
    	
    }
    
    // 준비된 기준정보를 적용 
    public static boolean applicationNewBaseRulse() {
    	try {
    		
    		FisPropertyObject.getInstance().setMappingRule(
	        		FisPropertyObject.getInstance().getPrepMappingRule()
	        		);
	        FisPropertyObject.getInstance().setParsingRule(
	        		FisPropertyObject.getInstance().getPrepParsingRule()
	        		);
    		
    		return true;
    	} catch (Exception e) {    		
    		log.info("## FisCommonUtil, applicationNewBaseRulse ", e);
    		return false;
    	}
    	
    }
    
    // 파일 유형당 쿼리 생성 
    private static String makeBatchInsertQuery(String fileType, String parsingObjectId, List<ParseRuleRelVo> mappingList) {
    	String query = FisPropertyObject.getInstance().getInsertBatchTemplate();
    	String dbColm = "";
    	String value = "";   	
//    	String fileType = "";
    	ParseRuleRelVo vo = null;
    	
    	log.info("## FisCommonUtil-makeBatchInsertQuery() -- in ");
    	log.info("## ObjectId:{} , mappingList.size :{}", parsingObjectId, mappingList.size());
    	
    	// TODO 쿼리 만든다!루프 돌아서, dbColm, value 만들기!
    	for( int i = 0 ; i < mappingList.size() ; i ++ ) {
    		vo = mappingList.get(i);
    		if ( parsingObjectId.equals( vo.getObjId()) ) {
    			dbColm += vo.getMpngClmNm();
    			value += "?";
    		} else {
    			log.info("");
    		}
    		
    		if ( i == (mappingList.size()-1) ) {
    			break;
    		} else {
    			dbColm += ",";
    			value += ",";
    		}
    		// TODO  file type from join parsing rule, param으로 fileType 가져오기!
    	}
    	
    	// Table Name Choise
    	if (fileType.equals(FisFileType.INSP.name()))
    		query.replace("TABLE_NAME", FisPropertyObject.getInstance().getTableNameInsp());
    	else
    		query.replace("TABLE_NAME", FisPropertyObject.getInstance().getTableNameMeas());
    	
    	query.replace("COLUM", dbColm);
    	query.replace("VALUE", value);
    	
    	log.info("## FisCommonUtil-makeBatchInsertQuery(), sql :" , query);
    	
    	return query;
    }
    
    // SQL 작성및 inserbatch 시 비교을 위한 Mapping Colum List 만들기 
    private static String[] getMappingColums(String objId, List<ParseRuleRelVo> mappingList) {
    	String[] mappingColums = null;	
    	ParseRuleRelVo vo = null;
    	int arrayCnt = 0;
    	
    	for( int i = 0 ; i < mappingList.size() ; i ++ ) {
    		if  ( objId.equals( vo.getObjId()) ) arrayCnt++;
    	}
    	
    	mappingColums = new String[arrayCnt];
    	
    	int j = 0;
    	for( int i = 0 ; i < mappingList.size() ; i ++ ) {
    		vo = mappingList.get(i);   
    		if  ( objId.equals( vo.getObjId()) ) {
    			mappingColums[j] = vo.getMpngClmNm();
    			j++;
    		} else 
    			;
    	}
    	
    	return mappingColums;
    }
    
    // inserbatch 시 number 형 data 타입 갯수 리턴 
    private static int[] getNumberDataTypeList(String objId, List<ParseRuleRelVo> mappingList) {
    	String numberColums = "";
    	String[] numberColumList = null;	
    	ParseRuleRelVo vo = null;
    	int cnt = 0;
    	
    	for( int i = 0 ; i < mappingList.size() ; i ++ ) {
    		vo = mappingList.get(i);
    		if  ( objId.equals( vo.getObjId()) && vo.getClmDataTyp().equals("NUMBER")) {
    			numberColums += String.valueOf(cnt);
    			numberColums += String.valueOf(",");
    			cnt++;
    		} else if  ( objId.equals( vo.getObjId()) ) {
    			cnt++;
    		} else
    			continue;
    		
    	}
    	if (numberColums.lastIndexOf(",") == numberColums.length()-1)
    		numberColumList = numberColums.substring(0, numberColums.length()-1).split(",");
    	else
    		numberColumList = numberColums.split(",");
    	
    	int[] returnList =  new int[numberColumList.length];
    	for( int i = 0 ; i < numberColumList.length ; i ++ ) {
    		returnList[i] = Integer.valueOf(numberColumList[i]);
    	}
    	
    	return returnList;
    }
    
    // inserbatch 시 number 형 data 타입 갯수 리턴 
    private static int[] getTimeStempTypeList(String objId, List<ParseRuleRelVo> mappingList) {
    	String timeStmpColums = "";
    	String[] timeStmpColumList = null;	
    	ParseRuleRelVo vo = null;
    	int cnt = 0;
    	
    	for( int i = 0 ; i < mappingList.size() ; i ++ ) {
    		vo = mappingList.get(i);
    		if  ( objId.equals( vo.getObjId()) && vo.getClmDataTyp().equals("TIMESTAMP")) {
    			timeStmpColums += String.valueOf(cnt);
    			timeStmpColums += String.valueOf(",");
    			cnt++;
    		} else if  ( objId.equals( vo.getObjId()) ) {
    			cnt++;
    		} else
    			continue;
    		
    	}
    	if (timeStmpColums.lastIndexOf(",") == timeStmpColums.length()-1)
    		timeStmpColumList = timeStmpColums.substring(0, timeStmpColums.length()-1).split(",");
    	else
    		timeStmpColumList = timeStmpColums.split(",");
    	
    	int[] returnList =  new int[timeStmpColumList.length];
    	for( int i = 0 ; i < timeStmpColumList.length ; i ++ ) {
    		returnList[i] = Integer.valueOf(timeStmpColumList[i]);
    	}
    	
    	return returnList;
    }
    
    // 쿼리 가져오는 method 
    public static String getQuery(String objId) {
    	String query = "";
    	List<ParseRuleVo> rule = FisPropertyObject.getInstance().getParsingRule();
    	
    	for ( int i = 0 ; i < rule.size() ; i++ ) {
    		if ( objId.equals(rule.get(i).getObjId()) ) {
    			query = rule.get(i).getQueryInsertBatch();
    			break;
    		} else {
    			continue;
    		}
    	}
    	
    	return query;
    }

	/**
	 * COLUMN (A,B,C,AA,AD,DV,..)를 index 숫자로 변환
	 * @param column
	 * @return
	 */
	public static int columnToIndex(String column) {
		int result = 0;
		int base = 26; // Number of letters in the alphabet

		for (int i = 0; i < column.length(); i++) {
			char c = column.charAt(i);
			result = result * base + (c - 'A' + 1);
		}

		// Adjusting to 0-based index
		return result - 1;
	}


	public static int changeClmTitlVal(String clmVal) {
    	int num = Integer.valueOf(clmVal);
    	return num - Integer.valueOf("A");
    }
    
    // file 유형의 objId 를 가져와서, 해당 vo 내에 있는 mappingColumList를 가져와야함.
    public static String[] getColums(String objId) {
    	String[] colums = null;
    	for (ParseRuleRelVo vo : FisPropertyObject.getInstance().getMappingRule()) {
    		if (vo.getObjId().equals(objId)) {
//    			colums = vo.();	// rule 에서 
    			break;
    		} else
    			continue;
    	}
    	return colums;
    }
    
    public static boolean checkDataInList(int[] dataTypeList, int val) {
    	for (int i = 0 ; i < dataTypeList.length ; i ++) {
    		if ( dataTypeList[i] == val)
    			return true;
    		else
    			continue;
    	}
    	return false;
    }
    
}
