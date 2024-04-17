package com.abs.cmn.fis.message.parse.impl;

import java.io.File;
import java.io.InvalidObjectException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.abs.cmn.fis.config.FisSftpPropertyObject;
import com.abs.cmn.fis.domain.work.model.CnFisWork;
import com.abs.cmn.fis.domain.work.service.ChFisWorkService;
import com.abs.cmn.fis.domain.work.vo.ChFisWorkSaveRequestVo;
import com.abs.cmn.fis.message.FisMessagePool;
import com.abs.cmn.fis.util.ToolCodeList;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.abs.cmn.fis.config.FisSharedInstance;
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
    private CnFisWorkService cnFisWorkService;

    @Autowired
    private ChFisWorkService chFisWorkService;
    @Autowired
    private FisFileMoveExecute filedelete;


    @Override
    public void init() {

    }

    @Async
    @Override
    public ExecuteResultVo execute(FisFileReportVo vo, String trackingKey) throws Exception {

        log.info("{} Start to parsing file. FisFileReportVo: {}",
                trackingKey, vo.toString());

        ExecuteResultVo resultVo = new ExecuteResultVo();
        long executeStartTime = System.currentTimeMillis();

        /**
         * Status `R`
         */
        String workId = this.createWorkId(trackingKey, vo);
        resultVo.setWorkId(workId);
        log.info("{} Start execute and insert work status. it's workId: {}",
                trackingKey, workId);

        String targetFilePath;
        if(!vo.getBody().getFilePath().startsWith("/")){
            /* 메세지 내에  윈도우 경로 \\ 를 입력 할 경우 JsonParser 오류가 나서, 임시로 대체 하여, 파싱 진행. / 경로는 오류 없음 */
            targetFilePath = this.modifyFilePath(trackingKey, FisSftpPropertyObject.getInstance().getApFileNasPathBase(),
                    vo.getBody().getEqpId(), vo.getBody().getFilePath());
            log.info("{} File path in message is not linux path. convert window path into linux.", trackingKey);
        }else{
            targetFilePath = vo.getBody().getFilePath();
        }
        log.info("{} Complete to set up target file path. Path: {}", trackingKey, targetFilePath);



        File file = null;
        try {
            file = this.fileManager.getFile(trackingKey, targetFilePath,
                    vo.getBody().getFileName());
        } catch (Exception e) {
            this.generateWorkHistoryAndUpdateState(workId, ProcessStateCode.RE);
            log.error("{} Error occur :{} , stop reading and print ResultVo: {}", trackingKey, e, resultVo.toString());
            FisMessagePool.messageAck(trackingKey);
            return resultVo;
        }
        log.info("{} Complete to get File object with target file path : {}", trackingKey, targetFilePath);
        resultVo.setTargetFilePath(targetFilePath);


        log.info("{} Success to access target file. Its' path: {}",
                trackingKey, file.getAbsolutePath());
        ParseRuleVo parsingRule = FisCommonUtil.getParsingRule(trackingKey, vo.getBody().getEqpId(),
                vo.getBody().getFileType());
        log.info("{} Success to get rule data. {}",trackingKey, parsingRule.toString());



        /**
         Status `P`
         */
        // 헤더 시작 위치 초기화
        int headerStartOffset = parsingRule.getHeaderStartValue();
        List<Map<String,String>> parsingResult;
        try{
            parsingResult = this.fileParser.parseCsvLine(trackingKey, resultVo, file,
                    headerStartOffset, workId, parsingRule);
        }catch (Exception e){

            this.generateWorkHistoryAndUpdateState(workId, ProcessStateCode.PE);
            log.error("{} Error occur :{} , stop parsing and print ResultVo: {}", trackingKey, e, resultVo.toString());
            FisMessagePool.messageAck(trackingKey);
            return resultVo;

        }
        this.generateWorkHistoryAndUpdateState(workId, ProcessStateCode.P);


        /**
         Status `I`
         */
        long dbInsertStartTime = System.currentTimeMillis();
        String status;
        try{

            status = this.parsingDataRepository.batchEntityInsert(
                    trackingKey, vo.getBody().getFileName(), workId, headerStartOffset,
                    parsingResult, parsingRule);

        }catch (Exception e){

            this.generateWorkHistoryAndUpdateState(workId, ProcessStateCode.IE);
            this.handleAbnormalCondition(trackingKey, vo, workId);
            log.error("{} Error occur :{} , stop parsing and print ResultVo: {}", trackingKey, e, resultVo.toString());
            FisMessagePool.messageAck(trackingKey);
            return resultVo;
        }



        resultVo.setInsertElapsedTime(System.currentTimeMillis() - dbInsertStartTime);
        this.generateWorkHistoryAndUpdateState(workId, ProcessStateCode.I);
        resultVo.setStatus(status);
        resultVo.setTotalElapsedTime(System.currentTimeMillis() - executeStartTime);




        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        /**
         Status `S`
         */
        // TODO EDC 메시지 송신:
        String sendCid;
        String messagePayload;
        String topicName;

        if(vo.getBody().getFileType().equals(FisFileType.INSPECTION.name()) || vo.getBody().getFileType().startsWith("I")){
            sendCid = FisMessageList.BRS_INSP_DATA_SAVE;

            messagePayload = mapper.writeValueAsString(this.setMessageObject(FisFileType.INSPECTION, sendCid, workId));

        }else if(vo.getBody().getFileType().equals(FisFileType.MEASUREMENT.name()) || vo.getBody().getFileType().startsWith("M")){
            sendCid = FisMessageList.BRS_MEAS_DATA_SAVE;

            messagePayload = mapper.writeValueAsString(this.setMessageObject(FisFileType.INSPECTION, sendCid, workId));

        }else{
            throw new InvalidObjectException(String.format("FileType is not undefined. FileType : {}. FileTypeEnums: {}"
                    , vo.getBody().getFileType(), FisFileType.values().toString()));
        }
        resultVo.setSendCid(sendCid);
        resultVo.setSendPayload(messagePayload);
        topicName = FisSharedInstance.getInstance().getSequenceManager().getTargetName("EDC", sendCid, messagePayload);
        InterfaceSolacePub.getInstance().sendTopicMessage(sendCid, messagePayload, topicName);
        log.info("{} Message Send. targetTopic: {}, sendCid: {}, payload: {}", trackingKey, topicName, sendCid,messagePayload );


        this.generateWorkHistoryAndUpdateState(workId, ProcessStateCode.S);


        log.info("{} Print ResultVo: {}", trackingKey, resultVo.toString());
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
    private void handleAbnormalCondition(String trackingKey, FisFileReportVo vo, String key) throws SQLException {

        String sql = FisCommonUtil.getDelteQuery(vo.getBody().getFileType());
        // 우선 삭제 진행
        this.parsingDataRepository.deleteBatch(
                trackingKey,
                vo.getBody().getFileType(),
                key,
                FisSharedInstance.getInstance().getBatchSize(),
                sql
        );	// 삭제 요청
    }

    /**
     * Insert work stat.
     * process stat default `R`
     * @param vo
     * @return
     */
    private String createWorkId(String trackingKey, FisFileReportVo vo){


        String typeString = vo.getBody().getFileType().startsWith("I") ? FisFileType.INSPECTION.name() : FisFileType.MEASUREMENT.name();

        String reqSystem = vo.getHead().getSrc();
        CnFisWorkSaveRequestVo cnFisWorkSaveRequestVo = CnFisWorkSaveRequestVo.builder()
                .trackingKey(trackingKey)
                .fileName(vo.getBody().getFileName())
                .filePath(vo.getBody().getFilePath())
                .fileType(typeString)
                .eqpId(vo.getBody().getEqpId())
                .requestSystemName(reqSystem)
                .processState(FisConstant.R.name())
                .createUserId(reqSystem).createDate(Timestamp.valueOf(LocalDateTime.now()))
                .updateUserId(reqSystem).updateDate(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        return this.cnFisWorkService.saveEntity(cnFisWorkSaveRequestVo).getObjId();
    }

    private void generateWorkHistoryAndUpdateState(String objId, ProcessStateCode stateCode){

        Optional<CnFisWork> cnFisWork = this.cnFisWorkService.getEntityByObjId(objId);

        if(cnFisWork.isPresent()){

            ChFisWorkSaveRequestVo chFisWorkSaveRequestVo = ChFisWorkSaveRequestVo.builder()
                    .refObjId(cnFisWork.get().getObjId())
                    .fileName(cnFisWork.get().getFileName())
                    .filePath(cnFisWork.get().getFilePath())
                    .fileType(cnFisWork.get().getFileType().name())
                    .eqpId(cnFisWork.get().getEqpId())
                    .requestSystemName(cnFisWork.get().getRequestSystemName())
                    .processState(cnFisWork.get().getProcessState().name())
                    .createUserId(cnFisWork.get().getCreateUserId())
                    .createDate(cnFisWork.get().getCreateDate())
                    .updateUserId(cnFisWork.get().getUpdateUserId())
                    .updateDate(cnFisWork.get().getUpdateDate())
                    .build();
            this.chFisWorkService.saveEntity(chFisWorkSaveRequestVo);

        }

        this.cnFisWorkService.updateEntity(objId, stateCode);
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
                return basePath + FisCommonUtil.detachToolNumber(eqpId) + linuxPath;

            default:
                return basePath + eqpId + linuxDelimiter + linuxPath;
        }


    }


}
