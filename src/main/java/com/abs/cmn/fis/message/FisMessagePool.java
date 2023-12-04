package com.abs.cmn.fis.message;

import com.solacesystems.jcsmp.BytesXMLMessage;
import com.solacesystems.jcsmp.XMLMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class FisMessagePool {



    private static ConcurrentHashMap<String, BytesXMLMessage> messageManageMap;

    public static ConcurrentHashMap<String, BytesXMLMessage> getMessageManageMap() {

        if (messageManageMap == null) {
            messageManageMap = new ConcurrentHashMap<String, BytesXMLMessage>();
        }
        return messageManageMap;
    }

    public FisMessagePool() {
        FisMessagePool.getMessageManageMap();
    }


    public static BytesXMLMessage getMessageObject(String messageId) {

        try {
            BytesXMLMessage message = messageManageMap.get(messageId);
            return message;
        }catch (Exception e) {

            log.error("{} message is not contained. error Message: {}. map: {}",
                    messageId, e.getMessage(), messageManageMap.toString());
            e.printStackTrace();
            throw new NullPointerException(String.format("%s message is not contained in Map.", messageId));
        }

    }

    public static String putMessageObject(String messageId, BytesXMLMessage messageObject){

        // TODO 동일한 messageId 진입 시, 대응 방안
        if(messageManageMap.contains(messageId)){
            String newMessageId = messageId.concat(String.valueOf(System.currentTimeMillis()));
            log.warn("{} messageId duplication. generate new id and do processing. new Message: {}",
                    messageId, newMessageId);
            messageManageMap.put(newMessageId, messageObject);
            return newMessageId;
        }

        messageManageMap.put(messageId, messageObject);
        return messageId;


    }


    /**
     * Message Acknowledge
     * @param messageId
     * @return
     */
    public static boolean messageAck(String messageId) {

        int orginCount = messageManageMap.size();
        try {

            BytesXMLMessage message = messageManageMap.get(messageId);

//            message.ackMessage();
            message.settle(XMLMessage.Outcome.ACCEPTED);

            if(message.isAckImmediately()){
                log.info("MessageID:{} has been acknowledged.", messageId);

            }

            if(messageManageMap.get(messageId) != null) {
                messageManageMap.remove(messageId);
            }else {
                log.warn("MessageID:{} is not contain in Map. ", messageId);
            }
            log.info("MessageID:{} has been acknowledged. And successfully remove in Map. Origin size: {}, Remain size in Map: {}",
                    messageId, String.valueOf(orginCount), String.valueOf(messageManageMap.size()));

            return true;

        }catch (Exception e) {

            log.error("Error while Acknowledge message. MessageID:{}  "
                    + " IsMessageContains:{}, IsAlreadyAcked?"
                    + "Error:{}." +  messageId, String.valueOf(messageManageMap.contains(messageId)), String.valueOf(messageManageMap.get(messageId).isAckImmediately()), e);
            e.printStackTrace();

            return false;
        }

    }




}
