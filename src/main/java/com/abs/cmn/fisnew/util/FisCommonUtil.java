package com.abs.cmn.fisnew.util;

import com.abs.cmn.fisnew.config.FisPropertyObject;
import com.abs.cmn.fisnew.domain.rule.model.CnFisIfParsingDataMappingInfo;
import com.abs.cmn.fisnew.domain.rule.model.CnFisIfParsingFileInfo;
import com.abs.cmn.fisnew.util.vo.ParsingRuleVo;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static List<ParsingRuleVo> convertMappingInfoInfoVo(List<CnFisIfParsingDataMappingInfo> entities){

        ArrayList<ParsingRuleVo> voList = new ArrayList<>();
        for(CnFisIfParsingDataMappingInfo e : entities){

            ParsingRuleVo vo = ParsingRuleVo.builder()
                    .eqpName(e.getEqpNm())
                    .fileFormatType(e.getFileFmTp())
                    .fileType(e.getFileTp())
                    .fileColumName(e.getFileClmNm())
                    .parsingColNum(e.getFileColmNum())
                    .mappColumnName(e.getMppClmNm())
                    .build();
            voList.add(vo);

        }
        return voList;

    }

    public static List<ParsingRuleVo> convertParsingInfoInfoVo(List<CnFisIfParsingFileInfo> entities){

        ArrayList<ParsingRuleVo> voList = new ArrayList<>();
        for(CnFisIfParsingFileInfo e : entities){

            String inputParsingTitleInfo = e.getPrsTtlInfo();
//            String [] parsingRowStringArray = FisCommonUtil.parsingArrayStringValues(inputParsingTitleInfo);
//            int[] parsingRowIntArray = FisCommonUtil.columnSequence(parsingRowStringArray);

            ParsingRuleVo vo = ParsingRuleVo.builder()
                    .eqpName(e.getEqpNm())
                    .fileFormatType(e.getFileTp())
                    .fileType(e.getFileTp())
                    .targetFileMovePath(e.getTgFileMvPt())
                    .parsingTitleInfo(inputParsingTitleInfo)
//                    .parsingTitleInfos(parsingRowStringArray)
//                    .parsingTitles(parsingRowIntArray)
                    .parsingRowInfo(e.getPrsRowInfo())
                    .build();

            voList.add(vo);


        }
        return voList;

    }

    public static int getParsingStartPoint(String eqpId, String fileType, String fileFormatType){
        // TODO 기준 정보 순회
        FisPropertyObject.getInstance().getParsingRule();

        return 3;

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

    // 값이 a-g 일 때 a ~ g 까지의  값을 모두 갖는 문자 배열을 리턴함  - 컬럼정보, 로우정보 둘다 배열화 할 때
    private static String[] parsingRangeInfos(String info) {
        // 'AD-AG' 값을 array[] = {'D', '-', 'G'} 로 변환
        log.info(info);
        String[] infos = info.split("-");
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

}
