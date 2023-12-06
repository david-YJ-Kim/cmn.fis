package com.abs.cmn.fis.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.abs.cmn.fis.domain.rule.service.CnFisIfParseRuleRelService;
import com.abs.cmn.fis.domain.rule.service.CnFisIfParseRuleService;

import com.abs.cmn.fis.util.code.FisConstant;
import org.springframework.beans.factory.annotation.Autowired;

import com.abs.cmn.fis.config.FisPropertyObject;
import com.abs.cmn.fis.domain.rule.model.CnFisIfParseRule;
import com.abs.cmn.fis.domain.rule.model.CnFisIfParseRuleRel;
import com.abs.cmn.fis.util.code.FisFileType;
import com.abs.cmn.fis.util.code.FisQueryValues;
import com.abs.cmn.fis.util.vo.ParseRuleRelVo;
import com.abs.cmn.fis.util.vo.ParseRuleVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FisCommonUtil {

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
            		.fileClmNumIntVal(changeClmTitlVal(e.getFileClmVal()))
                    .mpngClmNm(e.getMpngClmNm())
                    .clmDataTyp(e.getClmDataTyp())
                    .build();
            voList.add(vo);
            
            log.info("!! -- vo :: " + vo.toString());
        }
        return voList;

    }

    public static ParseRuleVo getParsingRule(String eqpId, String fileType){
        // TODO 기준 정보 순회

		String mapKey = FisCommonUtil.generateRuleStoreKey(eqpId, fileType);
		ParseRuleVo parsingRule = null;

		Map<String, ParseRuleVo> voMap = FisPropertyObject.getInstance().getRuleVoMap();
		if(voMap.containsKey(mapKey)) {
			parsingRule = voMap.get(mapKey);
		}else{
			log.warn("Vo is not in Map. Try to find with list");

			List<ParseRuleVo> rule = new ArrayList<>(voMap.values());
			for (ParseRuleVo vo : rule) {
				if ( vo.getEqpId().equals(eqpId) && vo.getFileTyp().name().equals(fileType)) {
					parsingRule = vo;
					break;
				} else {
					throw new NullPointerException(String.format("VO is not registered with condition. key: %s.  eqpId: %s. fileType: %s",
							mapKey, eqpId, fileType));
				}
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
    public static String[] parsingArrayStringValues(String info) {
        String[] ps = null;
        String result = "";

        if (info.indexOf(",") != -1 && info.indexOf("-") != -1) {
            // header info 에 ',' 와  '-' 함께 있을 때
            ps = info.split(",");
            String[] split = null;
            for(int i = 0 ; i < ps.length ; i ++) {
            	String oldInfo = ps[i];
                String newInfo = "";
                if( oldInfo.indexOf("-") != -1 ) {
                    // , ,의 값 사이에 있는   - 영역을 파싱 해옴
                    split = FisCommonUtil.parsingRangeInfos(oldInfo);

                    // 파싱해 온 값을 해당 열에 , 어레이 String 으로 저장
                    for (String c : split) {
                        newInfo += c.concat(",");
                    }
                }
//                newInfo.substring(0, newInfo.lastIndexOf(","));
//                log.info("******************************* "+oldInfo);
//                log.info("++++++++++++++++++++++++++++++ "+newInfo);
                result = result + newInfo;
//                log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ "+result);
            }
            
            result = result.substring(0, result.lastIndexOf(","));
log.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ "+result);
            // 변경 저장된 값으로 다시 나눔
            ps = result.split(",");
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
    public static int[] columnSequence(String[] parsingTitles) {
        int[] colSeqs = new int[parsingTitles.length];

        for (int i=0 ; i < parsingTitles.length ; i++ ) {
            colSeqs[i] = (int)parsingTitles[i].charAt(0) - (int)'A';
        }

        return colSeqs;
    }
    
    public static int[] setRowNumList(String[] parsingRows) {
    	int[] rowList = new int[parsingRows.length];
    	
    	for ( int i = 0 ; i< parsingRows.length ; i++ )
    		rowList[i] = Integer.valueOf(parsingRows[i]);
    	
    	return rowList;
    }

    // 값이 a-g 일 때 a ~ g 까지의  값을 모두 갖는 문자 배열을 리턴함  - 컬럼정보, 로우정보 둘다 배열화 할 때
    private static String[] parsingRangeInfos(String info) {
        // 'AD-AG' 값을 array[] = {'D', '-', 'G'} 로 변환 info = 'D-G' , '0-10000'
    	
        log.info(">> "+info);
        String[] infos = info.split("-");
        
        int size = 0;
        String[] result = null;
        
        // 정수 String 
		try  {
			int parsIntS = Integer.parseInt( infos[0] );
        	int parsIntE = Integer.parseInt(infos[1]);
			
        	log.info(">> parsIntS "+parsIntS+" parsIntE "+parsIntE);
        	size =  parsIntE - parsIntS;
        	log.info(">> size "+size);
        	result = new String[size];

            for ( int i = 0 ; i < size ; i++) {
                result[i] = i+Integer.valueOf(infos[0])+"";
            }
		} catch (NumberFormatException nfe) {
        // 알파벳 일 때,
    		int s = cvsCmlStrToInt(infos[0]);
    		int e = cvsCmlStrToInt(infos[1])+1;
        	size = e - s;
        	result = new String[size];
            for (int i = s ; i < e ; i++) {
            	result[i-s] = cvsCmlIntToStr( i );
            }
        }
        
        return result;
    }
    
    private static int cvsCmlStrToInt(String clm) {
    	int val = 0;
    	for (char ch : clm.toUpperCase().toCharArray())
    		val = val * 26 + (ch -'A'+1);
    	return val;
    }
    
    private static String cvsCmlIntToStr(int val) {
    	String str = "";
    	while ( val > 0 ) {
    		int idx = (val - 1) % 26; 
    		str = (char)(idx + 'A') + str;
    		val = ( val -1 )/ 26;
    	}
    	
    	return str;
    }
    
    public static boolean isNumber(String str) {
    	return str!=null && str.matches("[0-9.]+");// str.matches("[-+]?\\d#\\.?\\d+");
    }
    
    public static boolean reloadBaseRuleData() {
    	try {
	    	// DB에서 기준 정보 읽어옴
	    	List<CnFisIfParseRuleRel> mappingInfoEntities = cnFisIfParseRuleRelService.getAllEntities();
	        List<ParseRuleRelVo> mappingInfoVos = FisCommonUtil.convertParseRuleRelVo(mappingInfoEntities);
	        FisPropertyObject.getInstance().setPrepMappingRule(mappingInfoVos);
	
	        List<CnFisIfParseRule> parsingInfoEntities = cnFisIfParseRuleService.getAllEntities();
//	        List<ParseRuleVo> parsingInfoVos = FisCommonUtil.convertParseRuleVo(parsingInfoEntities, mappingInfoVos);
			List<ParseRuleVo> parsingInfoVos = null;
	        FisPropertyObject.getInstance().setPrepParsingRule(parsingInfoVos);
	        
	        // 현재 운영 룰 past에 저장 해 놓음
//	        FisPropertyObject.getInstance().setPastMappingRule(
//	        		FisPropertyObject.getInstance().getMappingRule()
//	        		);
//	        FisPropertyObject.getInstance().setPastParsingRule(
//	        		FisPropertyObject.getInstance().getParsingRule()
//	        		);
	        
	        return true;
        
    	} catch (Exception e) {
    		log.info("## FisCommonUtil, reloadBaseRuleData ", e);
    		return false;
    	}       
    	
    }
    
    // 준비된 기준정보를 적용 
    public static boolean applicationNewBaseRulse() {
    	try {
    		
//    		FisPropertyObject.getInstance().setMappingRule(
//	        		FisPropertyObject.getInstance().getPrepMappingRule()
//	        		);
//	        FisPropertyObject.getInstance().setParsingRule(
//	        		FisPropertyObject.getInstance().getPrepParsingRule()
//	        		);
    		
    		return true;
    	} catch (Exception e) {    		
    		log.info("## FisCommonUtil, applicationNewBaseRulse ", e);
    		return false;
    	}
    	
    }
    
    // 파일 유형당 쿼리 생성 
    public static String makeBatchInsertQuery(String fileType, String objId, List<ParseRuleRelVo> mappingList) {
    	String baseQuery = FisPropertyObject.getInstance().getInsertBatchTemplate();
    	String[] replaceStr1 = null;
    	String[] replaceStr2 = null;
    	String dbColm = "";
    	String value = "";
    	String returnSql = "";
    	ParseRuleRelVo vo = null;
    	
    	List<ParseRuleRelVo> myList = new ArrayList<ParseRuleRelVo>();
    	
    	for( int i = 0 ; i < mappingList.size() ; i ++ ) {
    		vo = mappingList.get(i);
    		if ( objId.equals( vo.getRefObjId()) ) {
    			myList.add(vo);
    		} else {
    			continue;
    		}
    	}
    	
    	// TODO 쿼리 만든다!루프 돌아서, dbColm, value 만들기!
    	for( int i = 0 ; i < myList.size() ; i ++ ) {
    		vo = myList.get(i);
    		if ( objId.equals( vo.getRefObjId()) ) {
    			dbColm += vo.getMpngClmNm();
    			value += "?";
    		} else {
    			log.info("");
    		}
    		
    		if ( i == (myList.size() - 1) ) {
    			break;
    		} else {
    			dbColm += ",";
    			value += ",";
    		}
    		// TODO  file type from join parsing rule, param으로 fileType 가져오기!
    	}
    	
    	replaceStr1 = baseQuery.split(FisQueryValues.TABLE_NAME.name());
    	// Table Name Choise
    	if (fileType.equals(FisFileType.INSP.name())) {
    		returnSql = replaceStr1[0]+FisPropertyObject.getInstance().getTableNameInsp()+replaceStr1[1];
    	} else {
    		returnSql = replaceStr1[0]+FisPropertyObject.getInstance().getTableNameMeas()+replaceStr1[1];
    	}
    	
    	replaceStr2 = returnSql.split( FisQueryValues.COLUM.name() );
    	
		String addClms = replaceStr2[0]+dbColm+replaceStr2[1];
		
		replaceStr1 = addClms.split(FisQueryValues.INPUT.name());
		
		returnSql = replaceStr1[0]+value+replaceStr1[1];

		log.info("ReturnSQL: {}",returnSql);
    	return returnSql;
    }
    
    // SQL 작성및 inserbatch 시 비교을 위한 Mapping Colum List 만들기

	/**
	 *
	 * @param objId : 기준정보의 키 값
	 * @param mappingList : 전체 릴레이션 entity 리스트
	 * @return
	 */
    public static String[] getMappingColumns(String objId, List<ParseRuleRelVo> mappingList) {


		String[] mappingColums = new String[mappingList.size()];
		ParseRuleRelVo vo = null;

		// String List를 Int List로
    	int j = 0;
    	for( int i = 0 ; i < mappingList.size() ; i ++ ) {

    		vo = mappingList.get(i);

    		if  ( objId.equals( vo.getRefObjId()) ) {

    			mappingColums[j] = vo.getMpngClmNm();
    			j++;
    		}
    	}


    	return mappingColums;
    }

	/**
	 * 해당 릴레이션의 데이터 타입을 체크해서
	 * 파라미터로 받은 데이터 타입에 부합하는 컬럼 리스트의 인덱스를 리턴
	 * @param sql
	 * @param dataType
	 * @param relatedRules
	 * @return
	 */
	public static ArrayList<Integer> getDataTypeList(String sql, String dataType, ArrayList<ParseRuleRelVo> relatedRules) {

		log.info("CheckDataType SQL: {}. Type: {}", sql, dataType);
		String parsStr = sql.substring(sql.indexOf("(") + 1); // A,B,C,D, VALUES
    	String colum = parsStr.substring(0, parsStr.indexOf("VALUES") - 2);  // A,B,C,D, SITEID, LOT_ID
    	String[] columList = colum.trim().split(","); // [A, B, C, , SITEID, LOT_ID]

		ArrayList<Integer> result = new ArrayList<>();

		for (int queryIdx = 0 ; queryIdx < columList.length ; queryIdx++ ) {

			for(ParseRuleRelVo ruleRelVo : relatedRules){

				// 1. 컬럼명이 같은가 >> 해당 컬럼명에 맞는 릴레이션 인지 확인
				String column = columList[queryIdx];
				if(column.equals(ruleRelVo.getMpngClmNm())){
					log.info("CheckDataType  column:{}, columnManppingName: {}", column, ruleRelVo.getMpngClmNm());

					// 2. 같다면) 원하는 데이터 타입인가.
					if(dataType.equals(ruleRelVo.getClmDataTyp())){

						log.info("CheckDataType  idx: {}. dataType:{}, getClmDataTyp: {}",queryIdx, dataType, ruleRelVo.getClmDataTyp());

						result.add(queryIdx);
					}

				}

			}

		}
		return result;

    }
    
    public static int changeClmTitlVal(String clmVal) {    	
    	int input, aVal = Integer.valueOf('A'), num = 0;
    	int cycleMax = Integer.valueOf('Z') - aVal;
    	
    	if (clmVal.length() < 2 ) {
    		input = Integer.valueOf( clmVal.charAt(0) );
    		num = input - aVal;
    	} else {
    		int val0 = Integer.valueOf( clmVal.charAt(0));
    		int val1 = Integer.valueOf( clmVal.charAt(1));

    		for (int i = 0 ; i < val0-aVal ; i ++ ) {
    			num += cycleMax;
        	}
    		num += (val1-aVal)+1;
    	} 
    	
    	return num;
    }
    
    // file 유형의 objId 를 가져와서, 해당 vo 내에 있는 mappingColumList를 가져와야함.
    public static String[] getColums(String objId) {
    	String[] colums = null;
		for (ParseRuleVo value : (FisPropertyObject.getInstance().getRuleVoMap().values())) {

    		if (value.getObjId().equals(objId)) {
//    			colums = vo.();	// rule 에서 
    			break;
    		} else
    			continue;
    	}
    	return colums;
    }
    
    public static boolean checkDataInList(int[] dataTypeList, int val) {

    	if ( dataTypeList != null ) {
	    	for (int i = 0 ; i < dataTypeList.length ; i ++) {
	    		if ( dataTypeList[i] == val)
	    			return true;
	    		else
	    			continue;
	    	}
    	} 
    	return false;
    }


	public static int[] convertToIntArray(ArrayList<Integer> arrayList) {
		int size = arrayList.size();
		int[] intArray = new int[size];

		for (int i = 0; i < size; i++) {
			intArray[i] = arrayList.get(i);
		}

		return intArray;
	}
    

	public static Timestamp stringToTimestamp(String value) throws ParseException {

    	Timestamp timestamp = Timestamp.valueOf(value);

    	return timestamp;

   	}

    
    public static void main(String[] args) {
    	String subNumberColums="INSERT INTO CN_FIS_INSP_DATA ( EQP_NM,FILE_FM_TPY,FILE_TPY,FILE_TRGT_POSN_VAL,PARS_CLM_ID_VAL,PARS_ROW_VAL,CRT_DT,SITE_ID,PROD_DEF_ID,PROC_DEF_ID,PROC_SGMT_ID,EQP_ID,LOT_ID,PROD_MTRL_ID,SUB_PROD_MTRL_ID,MTRL_FACE_CD,INSP_REPT_CNT,X_VAL,Y_VAL,GRD_ID,DFCT_ID,INSP_DT,IMG_FILE_NM,REVIEW_IMG_FILE_NM,INSP_FILE_NM,ATTR_1,ATTR_2,ATTR_N) VALUES ( ?,?,?,?,?,?,SYSTIMESTAMP,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";
//    	String[] numberColums = subNumberColums.split(" ( ");
    	String toPars = subNumberColums.substring(subNumberColums.indexOf("(")+1);
    	String colum = toPars.substring(0, toPars.indexOf(")")).trim();	// colum
    	
    	String toPars2 = toPars.substring(subNumberColums.indexOf("VALUES"));
    	System.out.println( subNumberColums.indexOf("(")+","+ subNumberColums.substring(subNumberColums.indexOf("(")+1) );
    	System.out.println(colum);
    	System.out.println(toPars2);
//    		System.out.println( numberColums[i] );
    	
    	
    }

	public static String generateRuleStoreKey(String eqpId, String fileTyp){
		return eqpId.concat(FisConstant._.name()) + fileTyp;
	}
}
