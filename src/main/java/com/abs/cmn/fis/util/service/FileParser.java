package com.abs.cmn.fis.util.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.abs.cmn.fis.util.FisCommonUtil;
import com.abs.cmn.fis.util.vo.ExecuteResultVo;
import com.abs.cmn.fis.util.vo.ParseRuleVo;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class FileParser {


    /**
     * Read file and save it in memory map.
     * @param trackingKey event tracking key
     * @param resultVo Result object, recognize each step.
     * @param file Target file object, need to parse.
     * @param headerStartOffset Content start row number in target file.
     * @param workId Key id that represent work status in db table (※ CN_FIS_WORK_INF).
     * @param parseRule Master data stored by eqpId & fileType (※ related table, CN_FIS_RULE)
     * @return Memory map data, filled with file content.
     */
    public List<Map<String, String>> parseCsvLine(String trackingKey, ExecuteResultVo resultVo, File file, int headerStartOffset,
                                                  String workId, ParseRuleVo parseRule) throws IOException{

        long fileReadStartTime = System.currentTimeMillis();
        // → Start time to calculating elapsed time.

        log.info("{} Start read file and parsing data." +
                        "Print parameters. headerOffset: {}, workId: {}",
                trackingKey, headerStartOffset, workId);

        BufferedReader bufferedReader = null;
        // → Reader object, filled with file content by using file IO.



        try {

            bufferedReader = new BufferedReader(new FileReader(file));

            resultVo.setFileReadElapsedTime(System.currentTimeMillis() - fileReadStartTime);
            log.info("{} Complete load file content at bufferedReader object.", trackingKey);


            long memoryLoadingStartTime = System.currentTimeMillis();
            // → Start time to calculating String into map memory.
            List<Map<String, String>> csvDataObjectList = this.generateCsvDataObjectList(
                    trackingKey, bufferedReader, headerStartOffset, workId,
                    parseRule,  parseRule.getParseClmIdValIntList(), parseRule.getParseRowValList()
            );


            resultVo.setRowCount(csvDataObjectList.size());
            resultVo.setParsingElapsedTime(System.currentTimeMillis() - memoryLoadingStartTime);
            log.info(">>>> csvDataObjectList size : "+csvDataObjectList.size());

            return csvDataObjectList;

        } catch (FileNotFoundException  e) {
            log.error("{} File Not Found Exception : {}}",trackingKey, e.getMessage(),  e);
            throw new NullPointerException(String.format("%s File not found. error message %s", trackingKey, e.getMessage()));

        } finally {

            if (bufferedReader != null ) {
                bufferedReader.close();
                log.info("{} BufferedReader is now closed.", trackingKey);
            }

        }

    }


    /**
     * Generate data object parsed csv file.
     * It's key-value format (key: colum, value: actual file value)
     *
     * @param trackingKey event tracking key
     * @param bufferedReader Reader object, filled with file content by using file IO.
     * @param headerStartOffset Content start row number in target file.
     * @param workId Key id that represent work status in db table (※ CN_FIS_WORK_INF).
     * @param parseRule Master data stored by eqpId & fileType (※ related table, CN_FIS_RULE)
     * @param columnValueList Int list represent colum in csv file (colum A → 0 / colum D → 4)
     * @param rowValueList Int list represent target row index at csv file.
     * @return csvDataObjectList, key-value data stored in memory.
     */
    private List<Map<String, String>> generateCsvDataObjectList(String trackingKey, BufferedReader bufferedReader,
                                                                int headerStartOffset, String workId, ParseRuleVo parseRule,
                                                                int[] columnValueList, int[] rowValueList)
            throws IOException {


        // TODO Header  row info 이해 불가...
        // Header Row info 필요
        int cnt = 0;
        if (headerStartOffset < 0 ){
            cnt = -1;
        }
        String[] columList = null;
        List<Map<String, String>> csvDataObjectList = new ArrayList<>();
        // → Memory map filled with file string by row.


        String csvLine = "";
        while ((csvLine = bufferedReader.readLine()) != null) {

            Map<String, String> csvLineObject = new HashMap<>();

            // 컬럼 짤라 오기, >> 컬럼을 숫자로 >>
            // column 정보  > 추후 colum info가 있는 line을  읽어야 함.

            if (cnt == headerStartOffset) {
                if ( headerStartOffset < 0 ) {
                    columList = parseRule.getMpngClmStrList();

                } else {
                    columList = new String[columnValueList.length];
                    String[] parsed = csvLine.split(",");
                    int j = 0;
                    for ( int i = 0 ; i < parsed.length ; i++ ) {
                        if ( FisCommonUtil.checkDataInList(columnValueList, i)) {
                            columList[j] = parsed[i];
                            j++;
                        } else continue;
                    }

                }
                cnt++;
                log.debug("{} [Colum] Count : {}, headerStartCount:{},  headerLine : {}",
                        trackingKey, cnt, headerStartOffset, csvLine);

            } else if(cnt > headerStartOffset && parseRule.getParseRowVal().equals("*") ){ // 로우 필터  >> *는 로우에서만
                log.debug("{} [*] cnt : {}, headerStartCount:{},  csvLine : {}",
                        trackingKey, cnt, headerStartOffset, csvLine);

                csvDataObjectList.add(this.generateCsvRowObject(workId, columList, csvLine,
                        csvLineObject, columnValueList)
                );
                cnt++;

            } else if(cnt > headerStartOffset && FisCommonUtil.checkDataInList(rowValueList, cnt) ){ // 로우 필터  >> *는 로우에서만

                log.debug("{} [list in] cnt : {}, headerStartCount:{},  csvLine : {}",
                        trackingKey, cnt, headerStartOffset, csvLine);
                csvDataObjectList.add(this.generateCsvRowObject(workId, columList, csvLine, csvLineObject, columnValueList));
                cnt++;

            }else{
                log.debug("{} [Row-Else] Count : {}, header:{},  csvLine : {}",
                        trackingKey, cnt, headerStartOffset, csvLine);
                cnt++;
            }
            log.debug("{} Add csvLine :{}, And data:{}",
                    trackingKey, cnt, csvLineObject.toString());
        }


        return csvDataObjectList;



    }


    /**
     *
     * @param workId
     * @param colList
     * @param csvLine
     * @param obj
     * @param clmValList
     * @return
     */
    private Map<String, String> generateCsvRowObject(String workId, String[] colList, String csvLine,
                                                     Map<String, String> obj, int[] clmValList){
        String[] rows = csvLine.split(",");

        obj.put("OBJ_ID", FisCommonUtil.generateObjKey());
        obj.put("WORK_ID", workId);
        obj.put("SITE_ID", "SVM");
        obj.put("CRT_DT", String.valueOf(Timestamp.valueOf(LocalDateTime.now())));

        int c = 0;
        for (int i = 0 ; i < rows.length ; i++ ){
            if ( FisCommonUtil.checkDataInList(clmValList, i) ) {
                obj.put(colList[c], rows[i]);
                log.debug("- "+i+" , "+colList[c]+" , "+rows[i]+" , i :"+i);
                c++;
            } else
                continue;
        }
        // 기준 정보를 읽어오는 방식으로 바뀐다.
        return obj;
    }



}
