package com.abs.cmn.fisnew.intf.solace;

import com.abs.cmn.fisnew.config.SolaceSessionConfiguration;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solacesystems.jcsmp.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
public class InterfaceSolacePub {
    private PubEventHandler pubEventHandler = new PubEventHandler();
    private Topic topic;
    private static InterfaceSolacePub instance;
    private JCSMPSession session;
    private XMLMessageProducer messageProducer;


    public InterfaceSolacePub() {
    }

    public void init() throws JCSMPException {

        SolaceSessionConfiguration sessionConfiguration = SolaceSessionConfiguration.getSessionConfiguration();
		session = sessionConfiguration.getSession("PUB");
        this.session.connect();

        this.messageProducer = this.session.getMessageProducer(new JCSMPStreamingPublishEventHandler() {
            public void responseReceived(String messageID) {
                System.out.println("Producer received response for msg: " + messageID);
            }
            public void handleError(String messageID, JCSMPException e, long timestamp) {
                System.out.printf("Producer received error for msg: %s@%s - %s%n",
                        messageID,timestamp,e);
            }
        });
    }

    public static InterfaceSolacePub getInstance() throws JCSMPException {
        // Create the instance if it doesn't exist
        if (instance == null) {
            instance = new InterfaceSolacePub();
        }

        return instance;
    }

    public void sendBasicTextMessage(String payload, String queueName){
        try{

            XMLMessageProducer prod = session.getMessageProducer(pubEventHandler);
            TextMessage txtMsg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);

            txtMsg.setText(payload);

            Queue queue = JCSMPFactory.onlyInstance().createQueue(queueName);

            prod.send(txtMsg, queue);
        }catch (Exception e){

            e.printStackTrace();
        }
    }

    public void sendMessage(HashMap<String, Object> sendMessage) {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
			XMLMessageProducer prod = session.getMessageProducer(pubEventHandler);

            //Custom Property
            SDTMap userPropMap = JCSMPFactory.onlyInstance().createMap();

            //Byte Message
            TextMessage txtMsg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);

//            //Get Reply head
//            BrsMsgHead replyHead = mapper.convertValue(sendMessage.get(BrsConstants.BRS_MSG_HEAD), BrsMsgHead.class);
//
//            topic = JCSMPFactory.onlyInstance().createTopic(SequenceManager.getTargetName(replyHead.getTgt()) );
//
//            //Custom Property 설정
//            userPropMap.putString(BrsConstants.MESSAGE_ID, SequenceManageUtil.generateMessageID());
//            userPropMap.putString(BrsConstants.CID, replyHead.getCid());

            txtMsg.setText(mapper.writeValueAsString(sendMessage));
            txtMsg.setProperties(userPropMap);
            txtMsg.setDeliveryMode(DeliveryMode.PERSISTENT);

            prod.send(txtMsg, topic);

		} catch(Exception e)
		{
//			log.error("Sender.sendMessage() Exception # ",e);
			if (session != null && !session.isClosed()) try { session.closeSession(); } catch (Exception e1) {}
		}
	}


    public class PubEventHandler implements JCSMPStreamingPublishCorrelatingEventHandler {
    	@Override
    	public void handleErrorEx(Object messageID, JCSMPException cause, long timestamp) {
    		log.error("Producer received error for msg. cause: {}", cause);
    	}

    	@Override
    	public void responseReceivedEx(Object messageID) {
    		log.info("Producer received response for msg.");

    	}
    }

    private Topic createTopic(String topicName) {
        return JCSMPFactory.onlyInstance().createTopic(topicName);
    }

    public boolean cleanUp() {
        if(!this.session.isClosed()) {
            this.session.closeSession();
        }
        return true;
    }
}
