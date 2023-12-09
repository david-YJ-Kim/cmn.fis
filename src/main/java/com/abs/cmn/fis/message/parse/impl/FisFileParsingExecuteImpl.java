package com.abs.cmn.fis.message.parse.impl;

import java.io.File;
import java.io.InvalidObjectException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.abs.cmn.fis.config.FisPropertyObject;
import com.abs.cmn.fis.domain.edm.model.CnFisEdcTmpVo;
import com.abs.cmn.fis.domain.edm.repository.ParsingDataRepository;
import com.abs.cmn.fis.domain.work.service.CnFisWorkService;
import com.abs.cmn.fis.domain.work.vo.CnFisWorkSaveRequestVo;
import com.abs.cmn.fis.intf.solace.InterfaceSolacePub;
import com.abs.cmn.fis.message.FisMessagePool;
import com.abs.cmn.fis.message.move.FisFileMoveExecute;
import com.abs.cmn.fis.message.parse.FisFileParsingExecute;
import com.abs.cmn.fis.message.vo.common.FisMsgHead;
import com.abs.cmn.fis.message.vo.receive.FisFileReportVo;
import com.abs.cmn.fis.message.vo.send.BrsInspDataSaveReqVo;
import com.abs.cmn.fis.message.vo.send.BrsMeasDataSaveReqVo;
import com.abs.cmn.fis.util.FisCommonUtil;
import com.abs.cmn.fis.util.FisMessageList;
import com.abs.cmn.fis.util.code.FisConstant;
import com.abs.cmn.fis.util.code.FisFileType;
import com.abs.cmn.fis.util.code.ProcessStateCode;
import com.abs.cmn.fis.util.service.FileManager;
import com.abs.cmn.fis.util.service.FileParser;
import com.abs.cmn.fis.util.vo.ExecuteResultVo;
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

    @Async
    @Override
    public ExecuteResultVo execute(FisFileReportVo vo, String ackKey) throws Exception {

        log.info("Start to parsing file. FisFileReportVo: {}", vo.toString());

        ExecuteResultVo resultVo = new ExecuteResultVo();
        long executeStartTime = System.currentTimeMillis();

        String key = this.createWorkId(vo);
        resultVo.setWorkId(key);
        
//        File file = this.fileManager.getFile(vo.getBody().getFilePath(), vo.getBody().getFileName());
        /* 메세지 내에  윈도우 경로 \\ 를 입력 할 경우 JsonParser 오류가 나서, 임시로 대체 하여, 파싱 진행. / 경로는 오류 없음 */
        String tmpPath = "D:\\\\documents\\\\dev-docs\\\\FIS-Sample-File\\\\";
        File file = this.fileManager.getFile(tmpPath, vo.getBody().getFileName());
        
        
        

        ParseRuleVo fileRule = FisCommonUtil.getParsingRule(vo.getBody().getEqpId(), vo.getBody().getFileType().name());
        
        // 헤더 시작 위치 초기화
        int headerStartOffset = fileRule.getStartHdrVal();

        List<Map<String,String>> parsingResult = this.fileParser.parseCsvLine(resultVo, file,
                                                            headerStartOffset, key, fileRule);

        // TODO Parsing : P 상태로 work table update

        long dbInsertStartTime = System.currentTimeMillis();
        String status = this.parsingDataRepository.batchEntityInsert(vo.getBody().getFileName(), key, headerStartOffset, parsingResult, fileRule);
        resultVo.setInsertElapsedTime(System.currentTimeMillis() - dbInsertStartTime);
        
        // TODO Insert : I 상태로 work table update
        this.workService.updateEntity(key, ProcessStateCode.I);
        
        // TODO status 의 정확한 역할 정의
        // 장애 케이스 식별  (Status가 key와 동일하지 하다면, 장애 )
    	if (status.equals(key)) {
            this.handleAbnormalCondition(vo, key);
    	}

        resultVo.setStatus(status);
        resultVo.setTotalElapsedTime(System.currentTimeMillis() - executeStartTime);

        log.info(resultVo.toString());


        // TODO EDC 메시지 송신:
        String sendCid = null;
        Object messageObject = null;

        if(vo.getBody().getFileType().equals(FisFileType.INSP)){
            log.info("INSP file. sendCid: {}", FisMessageList.BRS_INSP_DATA_SAVE);
            sendCid = FisMessageList.BRS_INSP_DATA_SAVE;

            messageObject = this.setMessageObject(FisFileType.INSP, sendCid, key);

        }else if(vo.getBody().getFileType().equals(FisFileType.MEAS)){
            log.info("Measre file. sendCid: {}", FisMessageList.BRS_MEAS_DATA_SAVE);
            sendCid = FisMessageList.BRS_MEAS_DATA_SAVE;

            messageObject = this.setMessageObject(FisFileType.INSP, sendCid, key);
            
        }else{
            throw new InvalidObjectException(String.format("FileType is not undefined. FileType : {}. FileTypeEnums: {}"
                    , vo.getBody().getFileType().name(), FisFileType.values().toString()));
        }
        resultVo.setSendCid(sendCid);
        resultVo.setSendPayload(messageObject.toString());
        InterfaceSolacePub.getInstance().sendTopicMessage(sendCid, messageObject.toString(), FisPropertyObject.getInstance().getSendTopicName());

        // TODO Insert : I 상태로 work table update
        this.workService.updateEntity(key, ProcessStateCode.M);
        
         // TODO 파일 이동
        // 이동한 폴더 패턴
        String moveFilePattern = "/base_path/${eqpId}/${date}";
//        String testMoveFolder = "C:\\Users\\DavidKim\\Desktop\\fis_test\\move_folder";
        String testMoveFolder = "D:\\MVD\\";


//        fileManager.moveFile(vo.getBody().getFilePath(), vo.getBody().getFileName(), testMoveFolder);
        fileManager.copyFile(vo.getBody().getFilePath(), vo.getBody().getFileName(), testMoveFolder,  vo.getBody().getFileName() + System.currentTimeMillis());

        resultVo.setMovedFilePath(testMoveFolder);
        resultVo.setMovedFileName(vo.getBody().getFileName());


        // TODO 메시지 Ack
        FisMessagePool.messageAck(ackKey);
        log.info("{} Complete processing. details: {}", key, resultVo.toString());


        // TODO 결과 status와 키 workId 리턴
        return resultVo;
    }

    private Object setMessageObject(FisFileType fileType, String sendCid, String key) throws InvalidObjectException {

        if(fileType.equals(FisFileType.INSP)){

            BrsInspDataSaveReqVo brsInspDataSaveReqVo = new BrsInspDataSaveReqVo();
            BrsInspDataSaveReqVo.BrsInspDataSaveReqBody body = new BrsInspDataSaveReqVo.BrsInspDataSaveReqBody();
            body.setWorkId(key);

            brsInspDataSaveReqVo.setHead(this.generateMsgHead(sendCid, FisConstant.BRS.name()));
            brsInspDataSaveReqVo.setBody(body);

            return brsInspDataSaveReqVo;

        }else if(fileType.equals(FisFileType.MEAS)){
            log.info("Measre file. sendCid: {}", FisMessageList.BRS_MEAS_DATA_SAVE);
            sendCid = FisMessageList.BRS_MEAS_DATA_SAVE;

            BrsMeasDataSaveReqVo brsMeasDataSaveReqVo = new BrsMeasDataSaveReqVo();
            BrsMeasDataSaveReqVo.BrsMeasDataSaveReqBody body = new BrsMeasDataSaveReqVo.BrsMeasDataSaveReqBody();
            body.setWorkId(key);

            brsMeasDataSaveReqVo.setHead(this.generateMsgHead(sendCid, FisConstant.BRS.name()));
            brsMeasDataSaveReqVo.setBody(body);

            return  brsMeasDataSaveReqVo;

        }else{
            throw new InvalidObjectException(String.format("FileType is not undefined. FileType : {}. FileTypeEnums: {}"
                    , fileType.name(), FisFileType.values().toString()));
        }
    }


    private FisMsgHead generateMsgHead(String cid, String tgt){
        FisMsgHead head = new FisMsgHead();
        head.setCid(cid);
        head.setSrc(FisConstant.FIS.name());
        head.setTgt(tgt);
        return head;
    }
    // 장애 대응 메소드
    private void handleAbnormalCondition(FisFileReportVo vo, String key) throws SQLException {

    	String sql = FisCommonUtil.getDelteQuery(vo.getBody().getFileType().name());
        // 우선 삭제 진행
        this.parsingDataRepository.deleteBatch(
        		vo.getBody().getFileType().name(),
        		key, 
        		FisPropertyObject.getInstance().getBatchSize(),
        		sql
        		);	// 삭제 요청
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
