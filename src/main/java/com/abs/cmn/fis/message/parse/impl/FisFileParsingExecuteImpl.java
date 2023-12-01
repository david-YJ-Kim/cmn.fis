package com.abs.cmn.fis.message.parse.impl;

import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.abs.cmn.fis.message.vo.receive.FisFileReportVo;
import com.abs.cmn.fis.util.code.FisFileType;
import com.abs.cmn.fis.util.vo.ExecuteResultVo;
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
    public ExecuteResultVo execute(FisFileReportVo vo) throws Exception {

        log.info("Start to parsing file. FisFileReportVo: {}", vo.toString());

        ExecuteResultVo resultVo = new ExecuteResultVo();
        long executeStartTime = System.currentTimeMillis();

        String key = this.createWorkId(vo);
        resultVo.setWorkId(key);
        
        File file = this.fileManager.getFile(vo.getBody().getFilePath(), vo.getBody().getFileName());


        ParseRuleVo fileRule = FisCommonUtil.getParsingRule(vo.getBody().getEqpId(), vo.getBody().getFileType().name());
        
        // 헤더 시작 위치 초기화
        int headerStartOffset = 0;
        if (fileRule.getParseRowValList() != null ){
            headerStartOffset = fileRule.getParseRowValList()[0];
        }


        List<Map<String,String>> parsingResult = this.fileParser.parseCsvLine(resultVo, file,
                                                            headerStartOffset, key, fileRule);

        // TODO Parsing : P 상태로 work table update

        String[] columList = parsingResult.get(0).keySet().toArray(new String[0]);
        log.info("Parsing result.  column List: {}. Its size : {}", Arrays.toString(columList), columList.length);


//        long dbInsertStartTime = System.currentTimeMillis();
//        String status = this.parsingDataRepository.batchEntityInsert(key, vo.getBody().getFileType(), parsingResult, fileRule);
//        resultVo.setInsertElapsedTime(System.currentTimeMillis() - dbInsertStartTime);
        
        // TODO Insert : I 상태로 work table update
        
        // TODO status 의 정확한 역할 정의
        // 장애 케이스 식별  (Status가 key와 동일하지 하다면, 장애 )
//    	if (status.equals(key)) {
//
//            this.handleAbnormalCondition(vo, key);
//    	} else ;
//
//
//        resultVo.setStatus(status);
        resultVo.setTotalElapsedTime(System.currentTimeMillis() - executeStartTime);

     // TODO 결과 status와 키 workId 리턴
        return resultVo;
    }
    
    // 장애 대응 메소드
    private void handleAbnormalCondition(FisFileReportVo vo, String key) throws SQLException {

        // 우선 삭제 진행
        this.parsingDataRepository.deleteBatch(vo.getBody().getFileType().name(), key, FisPropertyObject.getInstance().getBatchSize());	// 삭제 요청
    }

    private String createWorkId(FisFileReportVo vo){

        String reqSystem = vo.getHead().getSrc();
        // TODO WORK INFO 생성
        CnFisWorkSaveRequestVo cnFisWorkSaveRequestVo = CnFisWorkSaveRequestVo.builder()
                .fileName(vo.getBody().getFileName())
                .filePath(vo.getBody().getFilePath())
                .fileType(vo.getBody().getFileType().name())
                .eqpId(vo.getBody().getEqpId())
                .requestSystemName(reqSystem)
                .processState(FisConstant.R.name())
                .createUserId(reqSystem).createDate(Timestamp.valueOf(LocalDateTime.now()))
                .updateUserId(reqSystem).updateDate(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        return this.workService.saveEntity(cnFisWorkSaveRequestVo).getObjId();
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
