package com.abs.cmn.fisnew.intf.solace.broker;

import com.abs.cmn.fisnew.message.parse.FisFileParsingExecute;
import com.abs.cmn.fisnew.util.ApplicationContextProvider;
import com.abs.cmn.fisnew.util.FisMessageList;
import com.solacesystems.jcsmp.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Slf4j
public class Receiver implements Runnable {

//	private CountDownLatch latch;
	private JCSMPSession session;
	private ArrayList<String> queueList;
	private Queue queue;
	private EndpointProperties endPointProps;
	private FlowReceiver consumer;
//	private String module_name;
	private String thread_name;
	private String queue_name;

	public Receiver(JCSMPSession session, String thread_name, String queue_name) {
//		this.latch = latch;
		this.session = session;
//		this.queueList = queueList;
		this.queue_name = queue_name;
		this.thread_name = thread_name;
	}

	@Override
	public void run() {
		try {
			log.info("Receiver Thread Start # " + this.thread_name);

			// Queue - SolAdmin에서 생성한 queue에 접속, SolAdmin에 생성되지 않은 경우 Application에서 생성
			final Queue queue = JCSMPFactory.onlyInstance().createQueue(queue_name);

//			session.provision(queue, SessionConfiguration.getSessionConfiguration().getEndpoint(), JCSMPSession.FLAG_IGNORE_ALREADY_EXISTS);

			/* ConsumerFlow 설정 */
			final ConsumerFlowProperties flowProps = new ConsumerFlowProperties();
			// Queue에 연결할 flow 설정
			flowProps.setEndpoint(queue);
			// Manual Ack 설정
			flowProps.setAckMode(JCSMPProperties.SUPPORTED_MESSAGE_ACK_CLIENT);

			// FlowReceiver 생성
			consumer = session.createFlow(new MessageListener(this), flowProps, endPointProps);
			// FlowReceiver 실행(start를 해야 Endpoint로부터 메시지를 수신할 수 있음)
			consumer.start();
		} catch (InvalidPropertiesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JCSMPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public class MessageListener implements XMLMessageListener {
		public MessageListener(Receiver receiver) {
		}

		@SneakyThrows
		@Override
		public void onReceive(BytesXMLMessage message) {

			String filePath = "C:\\Workspace\\abs\\cmn\\fis-new\\src\\main\\resources\\";
			String fileName = "Absolics계측결과파일표준_20230918.csv";
			String fileType = "INSP";

			String fileFormatType = "FORMAT";
			String cid = "FIS_FILE_REQ"; // CID

			try{

				switch (cid){
					case FisMessageList.FIS_FILE_REQ:
						FisFileParsingExecute fisFileParsingExecute = ApplicationContextProvider.getBean(FisFileParsingExecute.class);
						fisFileParsingExecute.init();
						String key = fisFileParsingExecute.execute(fileType, fileName, filePath, "eqpId", "WFS", fileFormatType);
						break;
					case FisMessageList.FIS_INTF_COMP:

				}

			}catch (Exception e){

			}



			try {
//				log.info("================ Solace Method Before");
//				log.info("Request Time : " + CommonDate.getTimeUI(System.currentTimeMillis()));
//
//				log.info(message.getCorrelationId());
//				InterfaceSolacePub.getInstance().sendSelectorReply(message, "HelloWorld");
//				log.info("Send Complete");
//
//
//				// Set TraceID
//				MDC.put(MesConstants.TRACE_ID, UUID.randomUUID().toString());
//
////             log.info("dump: \n{}", message.dump());
//				SDTMap map = message.getProperties();
//				String body = "";
//				if (message instanceof TextMessage) {
//					body = ((TextMessage) message).getText();
//				} else if (message instanceof MapMessage) {
//					MapMessage msg = (MapMessage) message;
//
//				} else {
//					byte[] byteMsg = message.getBytes();
//					body = new String(byteMsg);
//
//				}
//				ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//				String cid = map.get(BrsConstants.CID) != null ? map.get(BrsConstants.CID).toString() : "";
//				String messageId = map.get(BrsConstants.MESSAGE_ID) != null ? map.get(BrsConstants.MESSAGE_ID).toString() : "";
//				log.info("Request MessageId : " + messageId);
//				log.info("Request CID : " + cid);
//				log.info("Request Body : " + body);
//
//				BrsMessageCommonUtils brsMessageCommonUtils = ApplicationContextProvider.getBean(BrsMessageCommonUtils.class);
//				BrsResponseIVO brsResponseIVO = new BrsResponseIVO();
//
//				message.ackMessage(); // manual ack mode 일 경우 명시적 추가
//				log.info("================ Solace Method Completed");
//				log.info("Response Time (Success): " + CommonDate.getTimeUI(System.currentTimeMillis()));
			} catch (Exception e) {
//				message.ackMessage();
//				log.error("Exception Message : " + ExceptionUtils.getMessage(e));
//				log.error("Exception StackTrace : " + ExceptionUtils.getFullStackTrace(e));
//
//				log.info("================ Solace Method Completed");
//				log.info("Response Time (Fail): " + CommonDate.getTimeUI(System.currentTimeMillis()));
//				log.info("Response Exception :" + e.toString());
//				throw e;
			}
		}

		@Override
		public void onException(JCSMPException exception) {
//			consumer.stop();
//			consumer.close();
//			latch.countDown();
			exception.printStackTrace();
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'onException'");
		}
	}
}
