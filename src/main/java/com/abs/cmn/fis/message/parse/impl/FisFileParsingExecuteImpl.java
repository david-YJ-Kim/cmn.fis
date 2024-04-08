package com.abs.cmn.fis.message.parse.impl;

import java.io.File;
import java.io.InvalidObjectException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.abs.cmn.fis.config.FisSftpPropertyObject;
import com.abs.cmn.fis.message.FisMessagePool;
import com.abs.cmn.fis.util.ToolCodeList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.abs.cmn.fis.config.FisPropertyObject;
import com.abs.cmn.fis.domain.edm.repository.ParsingDataRepository;
import com.abs.cmn.fis.domain.work.service.CnFisWorkService;
import com.abs.cmn.fis.domain.work.vo.CnFisWorkSaveRequestVo;
import com.abs.cmn.fis.intf.solace.InterfaceSolacePub;
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

    /**
     * File report message sequence.
     * 1. Status `R` : Receive message and create work stats
     * 2. Status `P` : Start read and parsing target file.
     * 3. Status `I` : Complete parsing and start insert data.
     * 4. Status `C` : Complete FIS work.
     */
    @Async
    @Override
    public ExecuteResultVo execute(FisFileReportVo vo, String trackingKey) throws Exception {

        log.info("{} Start to parsing file. FisFileReportVo: {}",
                trackingKey, vo.toString());

        ExecuteResultVo resultVo = new ExecuteResultVo();
        long executeStartTime = System.currentTimeMillis();

        /*
         * Status `R`
         */
        String workId = this.createWorkId(vo);
        resultVo.setWorkId(workId);
        log.info("{} Start execute and insert work status. it's workId: {}",
                trackingKey, workId);

        /* 메세지 내에  윈도우 경로 \\ 를 입력 할 경우 JsonParser 오류가 나서, 임시로 대체 하여, 파싱 진행. / 경로는 오류 없음 */
        String modifiedFilePath = this.modifyFilePath(trackingKey, FisSftpPropertyObject.getInstance().getApFileNasPathBase(),
                vo.getBody().getEqpId(), vo.getBody().getFilePath());
        File file = this.fileManager.getFile(trackingKey, modifiedFilePath,
                vo.getBody().getFileName());
        log.debug("{} Success to access target file. Its' path: {}",
                trackingKey, file.getAbsolutePath());
        ParseRuleVo parsingRule = FisCommonUtil.getParsingRule(trackingKey, vo.getBody().getEqpId(),
                vo.getBody().getFileType().name());
        log.debug("{} Success to get rule data. {}",trackingKey, parsingRule.toString());


        /*
          Status `P`
         */
        // 헤더 시작 위치 초기화
        int headerStartOffset = parsingRule.getHeaderStartValue();
        List<Map<String,String>> parsingResult = this.fileParser.parseCsvLine(trackingKey, resultVo, file,
                headerStartOffset, workId, parsingRule);
        this.workService.updateEntity(workId, ProcessStateCode.P);


        /*
            Status `I`
         */
        long dbInsertStartTime = System.currentTimeMillis();
        String status = this.parsingDataRepository.batchEntityInsert(
                vo.getBody().getFileName(), workId, headerStartOffset,
                parsingResult, parsingRule);
        resultVo.setInsertElapsedTime(System.currentTimeMillis() - dbInsertStartTime);
        this.workService.updateEntity(workId, ProcessStateCode.I);

        // TODO status 의 정확한 역할 정의
        // 장애 케이스 식별  (Status가 key와 동일하지 하다면, 장애 )
        if (status.equals(workId)) {
            this.handleAbnormalCondition(vo, workId);
        }

        resultVo.setStatus(status);
        resultVo.setTotalElapsedTime(System.currentTimeMillis() - executeStartTime);

        log.info(resultVo.toString());


        // TODO EDC 메시지 송신:
        String sendCid = null;
        Object messageObject = null;

        if(vo.getBody().getFileType().equals(FisFileType.INSPECTION)){
            log.info("INSP file. sendCid: {}", FisMessageList.BRS_INSP_DATA_SAVE);
            sendCid = FisMessageList.BRS_INSP_DATA_SAVE;

            messageObject = this.setMessageObject(FisFileType.INSPECTION, sendCid, workId);

        }else if(vo.getBody().getFileType().equals(FisFileType.MEASUREMENT)){
            log.info("Measre file. sendCid: {}", FisMessageList.BRS_MEAS_DATA_SAVE);
            sendCid = FisMessageList.BRS_MEAS_DATA_SAVE;

            messageObject = this.setMessageObject(FisFileType.INSPECTION, sendCid, workId);

        }else{
            throw new InvalidObjectException(String.format("FileType is not undefined. FileType : {}. FileTypeEnums: {}"
                    , vo.getBody().getFileType().name(), FisFileType.values().toString()));
        }
        resultVo.setSendCid(sendCid);
        resultVo.setSendPayload(messageObject.toString());
        InterfaceSolacePub.getInstance().sendTopicMessage(sendCid, messageObject.toString(), FisPropertyObject.getInstance().getSendTopicName());

        /*
            Status `C`
         */
        this.workService.updateEntity(workId, ProcessStateCode.C);


        FisMessagePool.messageAck(trackingKey);

        return resultVo;
    }

    private Object setMessageObject(FisFileType fileType, String sendCid, String key) throws InvalidObjectException {

        if(fileType.equals(FisFileType.INSPECTION)){

            BrsInspDataSaveReqVo brsInspDataSaveReqVo = new BrsInspDataSaveReqVo();
            BrsInspDataSaveReqVo.BrsInspDataSaveReqBody body = new BrsInspDataSaveReqVo.BrsInspDataSaveReqBody();
            body.setWorkId(key);

            brsInspDataSaveReqVo.setHead(this.generateMsgHead(sendCid, FisConstant.BRS.name()));
            brsInspDataSaveReqVo.setBody(body);

            return brsInspDataSaveReqVo;

        }else if(fileType.equals(FisFileType.MEASUREMENT)){
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

    /**
     * Insert work stat.
     * process stat default `R`
     * @param vo
     * @return
     */
    private String createWorkId(FisFileReportVo vo){

        String reqSystem = vo.getHead().getSrc();
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


    /**
     *
     * @param trackingKey
     * @param basePath
     * @param eqpId
     * @param windowFilePath Y:\\PROD_DEF_ID\\AP-TG-09\\PROC_DEF_ID
     * @return
     */
    public String modifyFilePath(String trackingKey, String basePath, String eqpId, String windowFilePath){

        String linuxDelimiter = "/";

        String linuxPath = FisCommonUtil.convertWindowPathToLinux(windowFilePath);
        switch (eqpId){
            case ToolCodeList.AP_TG_09_01:
            case ToolCodeList.AP_TG_10_01:
            case ToolCodeList.AP_OL_13_01:
            case ToolCodeList.AP_RD_11_01:
                return basePath + FisCommonUtil.detachToolNumber(eqpId) + linuxDelimiter + linuxPath;

            default:
                return basePath + eqpId + linuxDelimiter + linuxPath;
        }


    }


}
