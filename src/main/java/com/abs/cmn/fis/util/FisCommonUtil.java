package com.abs.cmn.fis.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.abs.cmn.fis.config.FisPropertyObject;
import com.abs.cmn.fis.domain.rule.model.CnFisParseRuleRel;
import com.abs.cmn.fis.util.code.FisConstant;
import com.abs.cmn.fis.util.code.FisFileType;
import com.abs.cmn.fis.util.code.FisQueryValues;
import com.abs.cmn.fis.util.vo.ParseRuleRelVo;
import com.abs.cmn.fis.util.vo.ParseRuleVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FisCommonUtil {

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

    public static List<ParseRuleRelVo> convertParseRuleRelVo(List<CnFisParseRuleRel> entities){

        ArrayList<ParseRuleRelVo> voList = new ArrayList<>();
        for(CnFisParseRuleRel e : entities){

            ParseRuleRelVo vo = ParseRuleRelVo.builder()
                    .objId(String.valueOf(e.getObjId()))
//                    .refObjId(String.valueOf(e.getRefObjId()))
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

    /**
     * Get Rule Vo in master data memory map
     * @param trackingKey event tracking key.
     * @param eqpId tool code
     * @param fileType type of file (INSPECTION || MEASURE)
     * @return Registered rule data in master data map.
     */
    public static ParseRuleVo getParsingRule(String trackingKey, String eqpId, String fileType){

        ParseRuleVo parsingRule = null;

        String mapKey = FisCommonUtil.generateRuleStoreKey(eqpId, fileType);

        Map<String, ParseRuleVo> voMap = FisPropertyObject.getInstance().getRuleVoMap();
        if(voMap.containsKey(mapKey)) {
            parsingRule = voMap.get(mapKey);
            log.info("{} Rule has been  registered with eqpId: {}, fileType: {}.",
                    trackingKey, eqpId, fileType);

        }else{
            log.warn("Vo is not in Map. Try to find with list");

            List<ParseRuleVo> rule = new ArrayList<>(voMap.values());
            for (ParseRuleVo vo : rule) {
                if (vo.getEqpId().equals(eqpId) && vo.getFileTyp().name().equals(fileType)) {
                    parsingRule = vo;
                    log.info("{} Rule has been found by looping the map list." +
                            "search conditon, eqpId: {}, fileType: {}",
                            trackingKey, eqpId, fileType);
                    break;

                } else {
                    throw new NullPointerException(
                            String.format("%s RuleVo is not registered with condition. key: %s.  eqpId: %s. fileType: %s",
                            trackingKey, mapKey, eqpId, fileType));
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

                    result += newInfo;
                } else {
                    result += oldInfo+",";
                }

//                log.info("******************************* "+oldInfo);
//                log.info("++++++++++++++++++++++++++++++ "+newInfo);
//                log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ "+result);
            }

            result = result.substring(0, result.length()-1);
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
        return str!=null && str.matches("[0-9.]+"); // str.matches("[-+]?\\d#\\.?\\d+");
    }

    /**
     * Generate insert query statement based on its file type, eqpId and base query from property
     * insert-template: INSERT INTO TABLE_NAME ( OBJ_ID,FILE_POSN_VAL,FILE_NM,WORK_ID,ROW_SEQ,CRT_DT,COLUM) VALUES ( ?,?,?,?,?,SYSTIMESTAMP,INPUT )
     * @param fileType Type (INSPECTION || MEASURE)
     * @param relationList Relation data from fis_rule_rel which same with eqpId & fileType
     * @return insert query statement
     */
    public static String makeBatchInsertQuery(FisFileType fileType, List<ParseRuleRelVo> relationList) {
        String baseQuery = FisPropertyObject.getInstance().getInsertBatchTemplate();
        log.info("Base query statement registered at application property. {}", baseQuery);

        String[] replaceStr1 = null;
        String[] replaceStr2 = null;

        StringBuilder registeredColumnsBuilder = new StringBuilder();
        StringBuilder queryValuesBuilder = new StringBuilder();


        String insertStatement = "";



        for( int i = 0 ; i < relationList.size() ; i ++ ) {
            ParseRuleRelVo vo = relationList.get(i);           // unit relation vo.
            registeredColumnsBuilder.append(vo.getMpngClmNm());        // mapping column
            queryValuesBuilder.append("?");

            if (i != relationList.size() - 1) {
                registeredColumnsBuilder.append(",");        // mapping column
                queryValuesBuilder.append(",");
            }
        }

        replaceStr1 = baseQuery.split(FisQueryValues.TABLE_NAME.name());
        // → ["INSERT INTO", "( OBJ_ID,FILE_POSN_VAL,FILE_NM,WORK_ID,ROW_SEQ,CRT_DT,COLUM) VALUES ( ?,?,?,?,?,SYSTIMESTAMP,INPUT )"]

        insertStatement = baseQuery.replace(FisQueryValues.TABLE_NAME.name(),
                        (fileType.name().equals(FisFileType.INSPECTION.name())
                                ? FisPropertyObject.getInstance().getTableNameInsp()
                                : FisPropertyObject.getInstance().getTableNameMeas()));

        // Table name chosen
//        if (fileType.name().equals(FisFileType.INSPECTION.name())) {
//            insertStatement = replaceStr1[0]+FisPropertyObject.getInstance().getTableNameInsp()+replaceStr1[1];
//        } else {
//            insertStatement = replaceStr1[0]+FisPropertyObject.getInstance().getTableNameMeas()+replaceStr1[1];
//        }

        replaceStr2 = insertStatement.split( FisQueryValues.COLUM.name() );
        // → ["INSERT INTO TABLE_NAME ( OBJ_ID,FILE_POSN_VAL,FILE_NM,WORK_ID,ROW_SEQ,CRT_DT,", ") VALUES ( ?,?,?,?,?,SYSTIMESTAMP,INPUT )"]

        String addClms = replaceStr2[0] + registeredColumnsBuilder + replaceStr2[1];

        replaceStr1 = addClms.split(FisQueryValues.INPUT.name());

        insertStatement = replaceStr1[0] + queryValuesBuilder + replaceStr1[1];

        log.info("ReturnSQL: {}",insertStatement);
        return insertStatement;
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


            mappingColums[j] = vo.getMpngClmNm();
            j++;
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

    public static String getDelteQuery(String type ) {
        String templat = FisPropertyObject.getInstance().getDeleteBatchTemplate();
        String query = null;
        String[] splt = templat.split(FisQueryValues.TABLE_NAME.name());

        if (type.equals(FisFileType.INSPECTION.name()))
            query = splt[0] + FisPropertyObject.getInstance().getTableNameInsp()+splt[1];
        else
            query = splt[0] + FisPropertyObject.getInstance().getTableNameMeas()+splt[1];

        return query;
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
    public static String convertDateFormat(String inputDate) {

        log.info(inputDate);

        if(inputDate.contains("/")){
            inputDate = inputDate.replaceAll("/", "-");
        }
        try {
            // Parse the input date string
//			SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
//			Date parsedDate = inputFormat.parse(inputDate);

            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date parsedDate = inputFormat.parse(inputDate);

            // Format the date into the desired format
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String formattedDate = outputFormat.format(parsedDate);

            // Print the formatted date
            log.info(formattedDate);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Timestamp stringToTimestamp(String value) throws ParseException {

        Timestamp timestamp = Timestamp.valueOf(value);

        return timestamp;

    }


    /**
     * Generate key with eqpId and fileType to store master data in memory map.
     * ex) eqpId + | + fileType → AP-TG-09-01|INSPECTION
     * @param eqpId: tool code ex) AP-TG-09-01
     * @param fileTyp: File type defined in FisFileType Class (INSPECTION || MEASURE)
     * @return master data key
     */
    public static String generateRuleStoreKey(String eqpId, String fileTyp){
        return eqpId + "|" + fileTyp;
    }


}
