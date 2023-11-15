package com.abs.cmn.fisnew.intf.solace;

import com.abs.cmn.fisnew.config.FisPropertyObject;
import com.abs.cmn.fisnew.config.SolaceSessionConfiguration;
import com.abs.cmn.fisnew.intf.solace.broker.Receiver;
import com.abs.cmn.fisnew.util.code.FisConstant;
import com.solacesystems.jcsmp.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

@Slf4j
public class InterfaceSolaceSub implements Runnable {

	private JCSMPSession session;
	private ArrayList<String> queueList;

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


    //////////////////////////////////////////////////////////////////////////// developed in Receiver.java

    /** Very simple static inner class, used for receives messages from Queue Flows. **/
	/*
	 * private static class QueueFlowListener implements XMLMessageListener {
	 *
	 * String queueName;
	 *
	 * public QueueFlowListener(String queueName) { this.queueName = queueName; }
	 *
	 * public void onReceive(BytesXMLMessage msg) { msgRecvCounter++; if
	 * (msg.getRedelivered()) { // useful check // this is the broker telling the
	 * consumer that this message has been sent and not ACKed before. // this can
	 * happen if an exception is thrown, or the broker restarts, or the netowrk
	 * disconnects // perhaps an error in processing? Should do extra checks to
	 * avoid duplicate processing hasDetectedRedelivery = true; }
	 *
	 *
	 * // TextMessage message = (TextMessage) msg; // System.out.println( //
	 * "##########################\n" + // "FROM THIS QUEUE : " +
	 * message.dump(message.getTopicNameOffset()) + // "MessageID : " +
	 * message.getMessageId() + "\n" + // "Message Text : " + message.getText() +
	 * "\n" + // "##########################" // ); System.out.println(
	 * "##########################\n" + "FROM THIS QUEUE : " + this.queueName +
	 * "##########################" );
	 *
	 * try { InterfaceSolacePub interfaceSolacePub =
	 * InterfaceSolacePub.getInstance();
	 * interfaceSolacePub.sendMessage(String.valueOf(msg)); } catch (JCSMPException
	 * e) { System.out.println(e.toString()); }
	 *
	 * // Messages are removed from the broker queue when the ACK is received. //
	 * Therefore, DO NOT ACK until all processing/storing of this message is
	 * complete. // NOTE that messages can be acknowledged from a different thread.
	 * msg.ackMessage(); // ACKs are asynchronous }
	 *
	 * public void onException(JCSMPException e) { // logger.warn("### Queue " +
	 * QUEUE_NAME + " Flow handler received exception.  Stopping!!", e); if (e
	 * instanceof JCSMPTransportException) { // all reconnect attempts failed
	 * isShutdown = true; // let's quit; or, could initiate a new connection attempt
	 * } else { // Generally unrecoverable exception, probably need to recreate and
	 * restart the flow flowQueueReceiver.close(); // add logic in main thread to
	 * restart FlowReceiver, or can exit the program } } }
	 */


}
