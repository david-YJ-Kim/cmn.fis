package com.abs.cmn.fis.util.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.abs.cmn.fis.util.FisCommonUtil;
import com.abs.cmn.fis.util.code.FisFileType;
import com.abs.cmn.fis.util.vo.ParseRuleRelVo;
import com.abs.cmn.fis.util.vo.ParseRuleVo;

import lombok.extern.slf4j.Slf4j;

import org.json.JSONObject;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class FileParser {


    public List<Map<String, String>> parsCsvLine(File file, int header, String workId, ParseRuleVo parseRule) throws IOException{

    	Charset charset = Charset.forName("UTF-8");	 // TODO 문의 : 확인 - 상위 단계에서 미리 설정해 놓을 수 있는지

        BufferedReader bufferedReader = null;
        List<Map<String, String>> listMapResult = null;
        String[] columList = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            if (bufferedReader != null) {
                log.info("Check reader exist - "+ bufferedReader.toString());
            }
            
            int[] clmValList = parseRule.getParseClmIdValIntList();
            int[] rowValeList = parseRule.getParseRowValList();
            if (parseRule.getParseRowVal().equals("*"))
            	header = 0;
            
            columList = new String[clmValList.length];
            
            listMapResult = new ArrayList<Map<String, String>>();
            
            // Header Row info 필요
            int cnt = 0;
            String csvLine = "";
            while ( (csvLine = bufferedReader.readLine()) != null ) {


                Map<String, String> csvLineObject = new HashMap<String, String>();

                // 컬럼 짤라 오기, >> 컬럼을 숫자로 >> 
                // column 정보  > 추후 colum info가 있는 line을  읽어야 함.
                if ( cnt == header ) {
                	String[] parsed = csvLine.split(",");
                	int j = 0;
                	for ( int i = 0 ; i < parsed.length ; i++ ) {
                		if ( FisCommonUtil.checkDataInList(clmValList, i)) {
                			columList[j] = parsed[i];
                			j++;
                		} else continue;
                	}
                    cnt++;
                    log.info("[Colum] Count : {}, headerStartCount:{},  headerLine : {}", cnt, header, csvLine);
                    
                } else if(cnt > header && parseRule.getParseRowVal().equals("*") ){ // 로우 필터  >> *는 로우에서만
                	
                	listMapResult.add(this.saveLineInMap(workId, columList, csvLine, csvLineObject, clmValList));
                    cnt++;
                    
                } else if(cnt > header && FisCommonUtil.checkDataInList(rowValeList, cnt) ){ // 로우 필터  >> *는 로우에서만 
                	
                	
                    listMapResult.add(this.saveLineInMap(workId, columList, csvLine, csvLineObject, clmValList));
                    cnt++;
                    log.info("[Row] Count : {}, header:{},  csvLine : {}", cnt, header, csvLine);

                }else{
                    log.info("[Row-Else] Count : {}, header:{},  csvLine : {}", cnt, header, csvLine);
                    cnt++;
                }

                log.info("Add csvLine :{}, And data:{}", cnt, csvLineObject.toString());
            }

        } catch (FileNotFoundException  e) {

            e.printStackTrace();
            log.error("## File Not Found Exception : read - ", e);
            return null;

        } finally {

            if (bufferedReader != null ) {
                bufferedReader.close();
                log.info("BufferedReader is closed.");
            }
            
        }
        return listMapResult;

    }

    private Map<String, String> saveLineInMap(String workId, String[] colList, String csvLine,
    							Map<String, String> obj, int[] clmValList){

        String[] rows = csvLine.split(",");
        int c = 0;
        for (int i = 0 ; i < rows.length ; i++ ){
        	if ( FisCommonUtil.checkDataInList(clmValList, i) ) {
        		obj.put(colList[c], rows[i]);
        		c++;
        	} else
        		continue;
        }
        // 기준 정보를 읽어오는 방식으로 바뀐다. 
        obj.put("OBJ_ID", FisCommonUtil.generateObjKey());
        obj.put("WORK_ID", workId);
        obj.put("SITE_ID", "SVM");
        obj.put("CRT_DT", String.valueOf(Timestamp.valueOf(LocalDateTime.now())));

        return obj;
    }


   
}
