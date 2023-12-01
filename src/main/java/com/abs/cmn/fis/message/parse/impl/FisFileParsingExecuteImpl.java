package com.abs.cmn.fis.message.parse.impl;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abs.cmn.fis.config.FisPropertyObject;
import com.abs.cmn.fis.domain.edm.model.CnFisEdcTmpVo;
import com.abs.cmn.fis.domain.edm.repository.ParsingDataRepository;
import com.abs.cmn.fis.domain.work.service.CnFisWorkService;
import com.abs.cmn.fis.domain.work.vo.CnFisWorkSaveRequestVo;
import com.abs.cmn.fis.message.move.FisFileMoveExecute;
import com.abs.cmn.fis.message.parse.FisFileParsingExecute;
import com.abs.cmn.fis.util.FisCommonUtil;
import com.abs.cmn.fis.util.code.FisConstant;
import com.abs.cmn.fis.util.service.FileManager;
//import com.abs.cmn.fisnew.util.service.FileParser;
import com.abs.cmn.fis.util.service.FileParser;
import com.abs.cmn.fis.util.vo.ParseRuleVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FisFileParsingExecuteImpl implements FisFileParsingExecute {


    @Autowired
    private FileManager fileManager;

    @Autowired
    private FileParser fileParser;

    @Autowired
    private ParsingDataRepository parsingDataRepository;

    @Autowired
    private CnFisWorkService workService;
    
    @Autowired
    private FisFileMoveExecute filedelete;


    @Override
    public void init() {

    }

    @Override
    public Map<String, String> execute(String fileType, String fileName, String filePath,
                                String eqpId, String reqSystem, String fileFormatType) throws Exception {

        log.info("Start to parsing file. type:{}, name:{}, path:{}", fileType, fileName, filePath);

        // TODO WORK INFO 생성
        CnFisWorkSaveRequestVo vo = CnFisWorkSaveRequestVo.builder()
                                    .fileName(fileName)
                                    .filePath(filePath)
                                    .fileType(fileType)
                                    .eqpId(eqpId)
                                    .requestSystemName(reqSystem)
                                    .processState(FisConstant.R.name())
                                    .createUserId(reqSystem).createDate(Timestamp.valueOf(LocalDateTime.now()))
                                    .updateUserId(reqSystem).updateDate(Timestamp.valueOf(LocalDateTime.now()))
                                    .build();
        String key = this.workService.saveEntity(vo).getObjId();
        
        // TODO 파일 읽기
        File file = this.fileManager.getFile(filePath, fileName);


        // TODO 파싱 시작 해더 가져오기
        // 빈차장 소스 머지
        ParseRuleVo fileRule = FisCommonUtil.getParsingRule(eqpId, fileType, fileFormatType);
        int startHeader = fileRule.getParseRowValList()[0];
        
        // TODO 파일 파싱하기
        long parsingStartTime = System.currentTimeMillis();
        List<Map<String,String>> parsingResult = this.fileParser.parsCsvLine(file, startHeader, key, fileRule);
        log.info("Parsing ElapsedTime: {}ms", System.currentTimeMillis() - parsingStartTime);
        long parsingTime = System.currentTimeMillis() - parsingStartTime;

        // TODO Parsing : P 상태로 work table update
        
        String[] columList = parsingResult.get(0).keySet().toArray(new String[0]);
        log.info(Arrays.toString(columList));

        // TODO 파싱 결과 > Vo로 변환 필요

        // TEST Entity
//        ArrayList<CnFisEdcTmpVo> entities = this.testList();
        
        // >> work history table  

        // TODO DB 적재
        String fileTypeConstant = fileType; // FisFileType.INSP.name(); // For Test
        // String status = this.parsingDataRepository.batchInsert(fileTypeConstant, parsingResult, key);

        long startTime = System.currentTimeMillis();
        String status = "";
        
        status = this.parsingDataRepository.batchEntityInsert(key, fileTypeConstant, parsingResult, fileRule);
        
     // TODO Insert : I 상태로 work table update
        
        // 장애 케이스 
    	if (status.equals(key)) { 
    		this.parsingDataRepository.deleteBatch(fileType, key, FisPropertyObject.getInstance().getBatchSize());	// 삭제 요청
    		// TODO 장애 케이스로 return 처리, 
    	} else ;
        
//        for (int i=0 ; i < Math.ceil((double)parsingResult.size()/1000); i ++) {
//        	status = this.parsingDataRepository.batchEntityInsert(key, fileTypeConstant, parsingResult);
//        	if (status.equals(key)) break;	// batchinsert 실패시 workId 반환
//        	else continue;
//        }
        
        log.info("DB Insert ElapsedTime: {}ms, Parsing Time: {}ms", System.currentTimeMillis() - startTime, parsingTime);
        log.info(status);

        // 리턴 데이터 
        log.info("Send key to EDC : {}", key);
        Map<String, String> response = new HashMap<String, String>();
        response.put("status", status);
        response.put("workId", key);
        
     // TODO 결과 status와 키 workId 리턴
        return response;
    }


    private ArrayList<CnFisEdcTmpVo> testList(){
        ArrayList<CnFisEdcTmpVo> list = new ArrayList<>();
        for (int i=0; i < 2; i++){

            CnFisEdcTmpVo vo = CnFisEdcTmpVo.builder()
                    .fileTp("FileType")
                    .fileFmTp("FileFormatType")
                    .siteId("SVM")
                    .prodDefId("PROD_DEF_ID")
                    .procDefId("PROC_DEF_ID")
                    .procSgmtId("PROC_SGM_ID")
                    .eqpId("EQP_ID")
                    .lotId("LOT_ID")
                    .prodMtrlId("PROD_MATRERIAL_ID")
                    .subProdMtrlId("SUB_PROD_MATERIAL_ID")
                    .mtrlFaceCd("TOP")
                    .inspReptCnt("1")
                    .xVal("X_VAL")
                    .yVal("Y_VAL")
                    .zVal("Z_VAL")
                    .dcitemId("DC_ITEM_ID")
                    .rsltVal("RESULT_VAL")
                    .grdId("GROUPD_ID")
                    .dfctId("DEFECT_ID")
                    .dfctXVal("DEFECT_X_VAL")
                    .dfctYVal("DEFECT_Y_VAL")
                    .inspDt("INSPECT_DT")
                    .imgFileNm("IMAGE_FILE_NAME")
                    .reviewImgFileNm("REVIEW_IMG_FILE_NM")
                    .inspFileNm("INSPECTION_FILE_NAME")
                    .attr1("ATRRIBUTE_1")
                    .attr2("ATRRIBUTE_2")
                    .attrN("ATTRIBUTE_N")
                    .crtDt(Timestamp.valueOf(LocalDateTime.now()))
                    .fileNm("FILE_NAME")
                    .build();
            list.add(vo);
        }
        return list;
    }
}
