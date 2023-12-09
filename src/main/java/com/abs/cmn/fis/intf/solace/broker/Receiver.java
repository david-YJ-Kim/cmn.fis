package com.abs.cmn.fis.intf.solace.broker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskRejectedException;

import com.abs.cmn.fis.config.FisPropertyObject;
import com.abs.cmn.fis.domain.rule.mng.CnFisIfRuleManager;
import com.abs.cmn.fis.domain.work.controller.FisWorkTableManageController;
import com.abs.cmn.fis.message.FisMessagePool;
import com.abs.cmn.fis.message.parse.FisFileParsingExecute;
import com.abs.cmn.fis.message.vo.receive.FisFileReportVo;
import com.abs.cmn.fis.message.vo.receive.FisTestMessageVo;
import com.abs.cmn.fis.util.ApplicationContextProvider;
import com.abs.cmn.fis.util.FisMessageList;
import com.abs.cmn.fis.util.code.FisConstant;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solacesystems.jcsmp.BytesXMLMessage;
import com.solacesystems.jcsmp.ConsumerFlowProperties;
import com.solacesystems.jcsmp.EndpointProperties;
import com.solacesystems.jcsmp.FlowReceiver;
import com.solacesystems.jcsmp.InvalidPropertiesException;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.JCSMPInterruptedException;
import com.solacesystems.jcsmp.JCSMPProperties;
import com.solacesystems.jcsmp.JCSMPSession;
import com.solacesystems.jcsmp.Queue;
import com.solacesystems.jcsmp.SDTMap;
import com.solacesystems.jcsmp.TextMessage;
import com.solacesystems.jcsmp.XMLMessageListener;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Receiver implements Runnable {


	private JCSMPSession session;
	private EndpointProperties endPointProps;
	private FlowReceiver consumer;
//	private String module_name;
	private String thread_name;
	private String queue_name;

	private boolean stopFlagOn = false;
	
	@Autowired
	private CnFisIfRuleManager cnFisIfRuleManager;
	
	public Receiver(JCSMPSession session, String thread_name, String queue_name) {
		this.session = session;
		this.queue_name = queue_name;
		this.thread_name = thread_name;
	}

	@Override
	public void run() {
		try {
			log.info("Receiver Thread Start # " + this.thread_name);

			// Queue - SolAdmin에서 생성한 queue에 접속, SolAdmin에 생성되지 않은 경우 Application에서 생성
			final Queue queue = JCSMPFactory.onlyInstance().createQueue(queue_name);

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

	private void switchStopFlag(){
		try {
			this.session.deleteSubscriber();
		} catch (JCSMPException e) {
			e.printStackTrace();
		}
		this.stopFlagOn = true;
	}


	public boolean stopReceiver() throws JCSMPInterruptedException {
 
//		this.consumer.stopSync();
		this.switchStopFlag();
		this.consumer.stop();
		log.info("Consumer Stop!!");
		return true;
	}

	public class MessageListener implements XMLMessageListener {
		public MessageListener(Receiver receiver) {
		}


		@SneakyThrows
		@Override
		public void onReceive(BytesXMLMessage message) {

			if(stopFlagOn){
				log.warn("Stop flag is on. Stop incoming request.");
				return;
			}

			ObjectMapper mapper = new ObjectMapper()
					.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true)
					.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);


			SDTMap userProperty = message.getProperties();
			String cid = userProperty.getString(FisConstant.cid.name());
			String key = userProperty.getString(FisConstant.messageId.name());
			String ackKey = FisMessagePool.putMessageObject(key, message);
			log.info("ackKey: {} is started managed in map : {}", ackKey, FisMessagePool.getMessageManageMap().size());

			try{

				// TODO 기준정보 초기화하는 것도 Executor로 분리
				if (message.getDestination().equals(FisPropertyObject.getInstance().getReceiveInitTopic())) {
					
					// TODO 파싱 기준 데이터  1. 리로딩 IF (CID 값으로 구분)
					// 				  2. 대체	ELSE
					cnFisIfRuleManager.init();
					
					if ( userProperty.getString(FisConstant.cid.name()).equals(FisConstant.RELOAD_RULE.name()) ) {
						cnFisIfRuleManager.reloadBaseRuleData();
					}
					else if ( userProperty.getString(FisConstant.cid.name()).equals(FisConstant.PATCH_RULE.name()) )
						cnFisIfRuleManager.applicationNewBaseRulse();
					else 
						log.error("## Receiver , onReceive() - Invalied Message ! check Messages : "+message.dump() );
					
				} else {
				
					String payload = "";
			

					if ( message instanceof TextMessage) {
						payload = ((TextMessage) message).getText();					
					} else {
						payload = new String( message.getBytes(), "UTF-8");
					}
					

					log.info("{} Incoming request: {}", ackKey, cid);
					switch (cid){
					case FisMessageList.FIS_TEST_MESSAGE:
						FisTestMessageVo fisTestMessageVo = mapper.readValue(payload, FisTestMessageVo.class);
						log.info("Request vo: {}", fisTestMessageVo.getBody().toString());

						try{
							Thread.sleep(fisTestMessageVo.getBody().getSleepTm());
						}catch (Exception e){
							e.printStackTrace();
						}

						break;
					case FisMessageList.FIS_FILE_REPORT:	// 파일 파싱 , 워크 생성 - R, 파일 저장, 

						FisFileReportVo fisFileReportVo = mapper.readValue(payload, FisFileReportVo.class);
						log.info("Request vo: {}", fisFileReportVo.getBody().toString());

						// TODO : Work table 상태 P (파싱)
						FisFileParsingExecute fisFileParsingExecute = ApplicationContextProvider.getBean(FisFileParsingExecute.class);
						fisFileParsingExecute.init();

						fisFileParsingExecute.execute(fisFileReportVo, ackKey);

						break;
					case FisMessageList.FIS_DLT_REQ:	// D 인 데이터 값 찾아서 History 로 적재 & 해당 ObjID 데이터 삭제
						FisWorkTableManageController workctlr = ApplicationContextProvider.getBean(FisWorkTableManageController.class); 
						workctlr.startDeleteLogic();
						message.ackMessage();
						break;
					default:
						log.error("## Invalied cid : "+cid);
						break;
					}

				}

			}catch(TaskRejectedException taskRejectedException){
				taskRejectedException.printStackTrace();
				log.error("Over capacity. It's overflow.");


			}catch (Exception e){
				e.printStackTrace();
				log.error("##  Receiver.onReceive() Exception : ", e);
			}

		}

		@Override
		public void onException(JCSMPException exception) {
			
			try {
				if ( session.isClosed()) session.connect();
				if ( consumer.isClosed()) consumer.start();
			} catch (JCSMPException e) {
				// TODO Auto-generated catch block
				log.error("## Receiver , onException : ",e);
			}

			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'onException'");
		}


	}
}
