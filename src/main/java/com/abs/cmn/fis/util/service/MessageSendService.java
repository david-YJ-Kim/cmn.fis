package com.abs.cmn.fis.util.service;

import com.abs.cmn.fis.config.FisSharedInstance;
import com.abs.cmn.fis.intf.solace.InterfaceSolacePub;
import com.abs.cmn.fis.util.vo.MessageSendRequestVo;
import com.abs.cmn.fis.util.vo.MessageSendResultVo;
import com.solacesystems.jcsmp.JCSMPException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class MessageSendService {


    public MessageSendResultVo sendTopicMessage(String trackingKey, MessageSendRequestVo requestVo, MessageSendResultVo resultVo){

        resultVo.setTrackingKey(trackingKey);
        resultVo.setMessageSendRequestVo(requestVo);

        String targetTopicName = null;
        try{
            targetTopicName = FisSharedInstance.getInstance().getSequenceManager().getTargetName(
                    requestVo.getTargetSystem(), requestVo.getEventName(), requestVo.getPayload()
            );
        }catch (Exception e){
            // TODO topic return fail... 대응 시나리오
            resultVo.setSuccessYn(false);
            return resultVo;
        }

        resultVo.setTargetTopic(targetTopicName);
        try {
            InterfaceSolacePub.getInstance().sendTopicMessage(
                    requestVo.getEventName(), requestVo.getPayload(), targetTopicName
            );
            resultVo.setMessageSendTime(FisSharedInstance.getInstance().getDateFormatter().format(new Date()));

        } catch (JCSMPException e) {
            // TODO message send fail... 대응 시나리오
            resultVo.setSuccessYn(false);
            throw new RuntimeException(e);
        }

        resultVo.setSuccessYn(true);
        return resultVo;

    }

    @Async
    public MessageSendResultVo sendAsyncTopicMessage(String trackingKey, MessageSendRequestVo requestVo, MessageSendResultVo resultVo){
        return this.sendTopicMessage(trackingKey, requestVo, resultVo);
    }
}
