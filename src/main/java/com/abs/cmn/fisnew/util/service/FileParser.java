package com.abs.cmn.fisnew.util.service;

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

import com.abs.cmn.fisnew.util.FisCommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class FileParser {





    public List<Map<String, String>> parsCsvLine(File file, int header, String workId) throws IOException{

        Charset.forName("UTF-8");	 // TODO 문의 : 확인 - 상위 단계에서 미리 설정해 놓을 수 있는지

        BufferedReader bufferedReader = null;
        List<Map<String, String>> listMapResult = null;
        String[] columList = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            if (bufferedReader != null) {
                log.info("Check reader exist - "+ bufferedReader.toString());
            }

            listMapResult = new ArrayList<Map<String, String>>();

            // Header Row info 필요
            int cnt = 0;
            String csvLine = "";
            while ( (csvLine = bufferedReader.readLine()) != null ) {


                Map<String, String> csvLineObject = new HashMap<String, String>();

                // column 정보  > 추후 colum info가 있는 line을  읽어야 함.
                if ( cnt == header ) {

                    columList = csvLine.split(",");
                    cnt++;
                    log.info("[Colum] Count : {}, headerStartCount:{},  headerLine : {}", cnt, header, csvLine);

                } else if(cnt > header){

                    listMapResult.add(this.saveLineInMap(workId, columList, csvLine, csvLineObject));
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
            return listMapResult;

        }

    }

    private Map<String, String> saveLineInMap(String workId, String[] colList, String csvLine, Map<String, String> obj){

        String[] rows = csvLine.split(",");

        for (int i = 0 ; i < rows.length ; i++ ){

            obj.put(colList[i], rows[i]);
        }
        obj.put("OBJ_ID", FisCommonUtil.generateObjKey());
        obj.put("WORK_ID", workId);
        obj.put("SITE_ID", "SVM");
        obj.put("CRT_DT", String.valueOf(Timestamp.valueOf(LocalDateTime.now())));

        return obj;
    }


}
