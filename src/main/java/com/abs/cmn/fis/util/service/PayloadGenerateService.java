package com.abs.cmn.fis.util.service;

import com.abs.cmn.fis.config.FisSharedInstance;
import com.abs.cmn.fis.message.vo.common.FisMsgHead;
import com.abs.cmn.fis.message.vo.send.BrsInspDataSaveReqVo;
import com.abs.cmn.fis.message.vo.send.BrsMeasDataSaveReqVo;
import com.abs.cmn.fis.util.FisMessageList;
import com.abs.cmn.fis.util.code.FisConstant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PayloadGenerateService {

    public String generateBrsInspDataSave(String workId, String lotId) throws JsonProcessingException {
        String eventName = FisMessageList.BRS_INSP_DATA_SAVE;

        BrsInspDataSaveReqVo brsInspDataSaveReqVo = new BrsInspDataSaveReqVo();
        BrsInspDataSaveReqVo.BrsInspDataSaveReqBody body = new BrsInspDataSaveReqVo.BrsInspDataSaveReqBody();
        body.setSiteId(FisSharedInstance.getInstance().getSiteName());
        body.setUserId(FisSharedInstance.getInstance().getGroupName());
        body.setWorkId(workId);
        body.setLotId(lotId);
        brsInspDataSaveReqVo.setHead(this.generateMessageHead(eventName, FisConstant.EDC.name()));
        brsInspDataSaveReqVo.setBody(body);

        return this.convertObjectIntoString(brsInspDataSaveReqVo);

    }


    public String generateBrsMeasDataSave(String workId, String lotId) throws JsonProcessingException {
        String eventName = FisMessageList.BRS_MEAS_DATA_SAVE;

        BrsMeasDataSaveReqVo brsMeasDataSaveReqVo = new BrsMeasDataSaveReqVo();
        BrsMeasDataSaveReqVo.BrsMeasDataSaveReqBody body = new BrsMeasDataSaveReqVo.BrsMeasDataSaveReqBody();
        body.setSiteId(FisSharedInstance.getInstance().getSiteName());
        body.setUserId(FisSharedInstance.getInstance().getGroupName());
        body.setWorkId(workId);
        body.setLotId(lotId);
        brsMeasDataSaveReqVo.setHead(this.generateMessageHead(eventName, FisConstant.EDC.name()));
        brsMeasDataSaveReqVo.setBody(body);

        return this.convertObjectIntoString(brsMeasDataSaveReqVo);

    }

    private FisMsgHead generateMessageHead(String eventName, String targetSystem){
        FisMsgHead head = new FisMsgHead();
        head.setCid(eventName);
        head.setSrc(FisConstant.FIS.name());
        head.setTgt(targetSystem);
        return head;
    }

    private String convertObjectIntoString(Object messageObject) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.writeValueAsString(messageObject);
    }
}
