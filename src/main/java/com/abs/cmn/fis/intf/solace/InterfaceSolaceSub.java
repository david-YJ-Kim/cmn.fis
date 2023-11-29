package com.abs.cmn.fis.intf.solace;

import com.abs.cmn.fis.config.FisPropertyObject;
import com.abs.cmn.fis.config.SolaceSessionConfiguration;
import com.abs.cmn.fis.intf.solace.broker.Receiver;
import com.abs.cmn.fis.util.code.FisConstant;
import com.solacesystems.jcsmp.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

@Slf4j
public class InterfaceSolaceSub implements Runnable {

	private JCSMPSession session;

    private static final String SAMPLE_NAME = InterfaceSolaceSub.class.getSimpleName();
    private static String QUEUE_NAME = "SVM_DEV_BRS_LOT_00";		// used queue name ex ) PROP_RMP_00
    private static final String API = "JCSMP";
    private static volatile int msgRecvCounter = 0;                 // num messages received
    private static volatile boolean hasDetectedRedelivery = false;  // detected any messages being redelivered?
    private static volatile boolean isShutdown = false;             // are we done?
    private final JCSMPSession subSession;
    private static FlowReceiver flowQueueReceiver;
    private final Queue queue;
    private final ConsumerFlowProperties consumerFlowProperties;

    public InterfaceSolaceSub() throws JCSMPException {

        // init subscribe
//        QUEUE_NAME = queueName;
//        this.subSession = subSession;
//        this.subSession.connect();
//        this.queue = JCSMPFactory.onlyInstance().createQueue(queueName);
//        this.consumerFlowProperties = this.setConsumerFlowProperties();

		subSession = null;
		queue = null;
		consumerFlowProperties = null;
	}

    @Override
    public void run() {
        try {
            start();
        } catch (JCSMPException | IOException | InterruptedException e) {
            log.error(e.toString());
        }
    }

    private void start() throws JCSMPException, IOException, InterruptedException {

        try {


			SolaceSessionConfiguration sessionConfiguration = SolaceSessionConfiguration.getSessionConfiguration();
			session = sessionConfiguration.getSession("SUB");
			//session 연결 - Application별로 최소 연결 권장(쓰레드를 사용할 경우 공유 사용 권장)
			session.connect();

			FisPropertyObject obj = FisPropertyObject.getInstance();

			String threadName = obj.getClientName() + "-" + FisConstant.receiver.name();
			String receiveQueueName = obj.getReceiveQueueName();

			Receiver receiver = new Receiver(session, threadName, receiveQueueName);

			Thread thread = new Thread(receiver);
			thread.start();

        } catch (OperationNotSupportedException | JCSMPErrorResponseException e) {  // not allowed to do this
			log.error(e.toString());
        }
    }

    // ##################################################

    public boolean stopFlowQueueReceiver(){
        flowQueueReceiver.stop();
        return true;
    }

    /**
     * Call SEQ Library
     * @return
     */
	private String getTopicName(String targetSystem, String payload, String eventId, String eqpId) {
		String[] digits = {"00", "01", "02"};
		int randomIndex = new Random().nextInt(digits.length);
		return "SVM/DEV/WFS/CMN/" + digits[randomIndex];
	}


	private Topic createTopic() {
		String topicName = "Return From SEQ Library";
		return this.createTopic(topicName);
	}


    private Topic createTopic(String topicName) {
        return JCSMPFactory.onlyInstance().createTopic(topicName);

    }

    public boolean cleanUp(JCSMPSession session) {


//        if(!this.flowQueueReceiver.isClosed()) {
//            this.flowQueueReceiver.close();
//        }

        if(!session.isClosed()) {
            session.closeSession();
        }

        return true;
    }

}
