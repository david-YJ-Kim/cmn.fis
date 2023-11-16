package com.abs.cmn.fis.message.parse.impl;

import com.abs.cmn.fis.domain.edm.model.CnFisEdcTmpVo;
import com.abs.cmn.fis.domain.edm.repository.ParsingDataRepository;
import com.abs.cmn.fis.domain.work.service.CnFisWorkService;
import com.abs.cmn.fis.domain.work.vo.CnFisWorkSaveRequestVo;
import com.abs.cmn.fis.message.parse.FisFileParsingExecute;
import com.abs.cmn.fis.util.code.FisConstant;
import com.abs.cmn.fis.util.service.FileManager;
//import com.abs.cmn.fisnew.util.service.FileParser;
import com.abs.cmn.fis.util.service.FileParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String key = this.workService.saveEntity(vo).getWorkId();


        // TODO 파일 읽기
        File file = this.fileManager.getFile(filePath, fileName);


        // TODO 파싱 시작 해더 가져오기
        // 빈차장 소스 머지
//        int startHeader = FisCommonUtil.getParsingStartPoint(eqpId, fileType, fileFormatType);
        int startHeader = 0;

        // TODO 파일 파싱하기
        List<Map<String,String>> parsingResult = this.fileParser.parsCsvLine(file, startHeader, key);
        log.debug(parsingResult.toString());

        String[] columList = parsingResult.get(0).keySet().toArray(new String[0]);
        log.info(Arrays.toString(columList));

        // TODO 파싱 결과 > Vo로 변환 필요

        // TEST Entity
//        ArrayList<CnFisEdcTmpVo> entities = this.testList();


        // TODO DB 적재
        String fileTypeConstant = FisConstant.Inpection.name(); // For Test
        // String status = this.parsingDataRepository.batchInsert(fileTypeConstant, parsingResult, key);

        long startTime = System.currentTimeMillis();
        String status = this.parsingDataRepository.batchEntityInsert(key, fileTypeConstant, parsingResult);
        log.info("ElapsedTime: {}ms", System.currentTimeMillis() - startTime);
        log.info(status);

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
