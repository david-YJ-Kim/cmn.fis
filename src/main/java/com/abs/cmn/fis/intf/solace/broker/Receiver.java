package com.abs.cmn.fis.intf.solace.broker;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.ws.Response;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;

import com.abs.cmn.fis.config.FisPropertyObject;
import com.abs.cmn.fis.intf.solace.InterfaceSolacePub;
import com.abs.cmn.fis.message.move.FisFileMoveExecute;
import com.abs.cmn.fis.message.parse.FisFileParsingExecute;
import com.abs.cmn.fis.util.ApplicationContextProvider;
import com.abs.cmn.fis.util.FisCommonUtil;
import com.abs.cmn.fis.util.FisMessageList;
import com.abs.cmn.fis.util.code.FisConstant;
import com.solacesystems.jcsmp.BytesXMLMessage;
import com.solacesystems.jcsmp.ConsumerFlowProperties;
import com.solacesystems.jcsmp.EndpointProperties;
import com.solacesystems.jcsmp.FlowReceiver;
import com.solacesystems.jcsmp.InvalidPropertiesException;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPFactory;
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
	private ArrayList<String> queueList;
	private Queue queue;
	private EndpointProperties endPointProps;
	private FlowReceiver consumer;
//	private String module_name;
	private String thread_name;
	private String queue_name;
	
    private InterfaceSolacePub interfaceSolacePub;


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

//			String filePath = "D:\\work-spaces\\FIS-work-space\\fis\\src\\main\\resources\\";
//			String fileName = "Absolics계측결과파일표준_20230918.csv";
//			String fileType = "INSP";
//			String fileFormatType = "FORMAT";
//			String cid = "FIS_FILE_REQ"; // CID
			

			try{
				String cid = null;				
				SDTMap userProperty = message.getProperties();				
				
				if (message.getDestination().equals(FisPropertyObject.getInstance().getReceiveInitTopic())) {
					
					// TODO 파싱 기준 데이터  1. 리로딩 IF (CID 값으로 구분)
					// 				  2. 대체	ELSE
					if ( userProperty.getString("cid").equals(FisConstant.RELOAD_RULE.name()) )
						FisCommonUtil.reloadBaseRuleData();
					else if ( userProperty.getString("cid").equals(FisConstant.PATCH_RULE.name()) )
						FisCommonUtil.applicationNewBaseRulse();
					else 
						log.error("## Receiver , onReceive() - Invalied Message ! check Messages : "+message.dump() );
					
				} else {
				
					JSONObject msg = null;
					String payload = "";
			
					log.info("@@ dump : "+message.dump());
					cid = userProperty.getString("cid");
					
					log.info("cid "+userProperty.getString("cid") );
					
					if ( message instanceof TextMessage) {
						payload = ((TextMessage) message).getText();					
					} else {
						payload = new String( message.getBytes(), "UTF-8");
					}
					
//					payload ="{\r\n" + 
//							"	\"body\":{\r\n" + 
//							"		\"fileType\":\"INSP\",\r\n" + 
//							"		\"filePath\":\"D:\\\\work-spaces\\\\FIS-work-space\\\\fis\\\\src\\\\main\\\\resources\\\\\",\r\n" + 
//							"		\"fileName\":\"검사결과_파일표준_20231116.csv\",\r\n" + 
//							"		\"eqpId\":\"AM-YG-09-01\",\r\n" + 
//							"		\"userId\":\"BRS\",\r\n" + 
//							"		\"fileFormatType\":\"FORMAT\",\r\n" + 
//							"	}\r\n" + 
//							"}"; 
							
					
					log.info("payload: "+payload);
					
					msg = new JSONObject(payload);
										
					if (msg != null) log.info("msg : "+msg.toString());
					
					JSONObject msgbody = new JSONObject(msg.get("body").toString());
					
					log.info("msgbody : "+ msgbody.toString());
					
					String fileType =  msgbody.getString("fileType"); 
					String fileName = msgbody.getString("fileName");
					String filePath = msgbody.getString("filePath");
					String eqpId = msgbody.getString("eqpId");
					String reqSystem = msgbody.getString("userId");
					String fileFormatType = msgbody.getString("fileFormatType");

					switch (cid){
					case FisMessageList.FIS_FILE_REQ:
						// TODO : Work table 상태 P (파싱)
						FisFileParsingExecute fisFileParsingExecute = ApplicationContextProvider.getBean(FisFileParsingExecute.class);
						fisFileParsingExecute.init();
						Map<String, String> response = fisFileParsingExecute.execute(fileType, fileName, filePath, eqpId, reqSystem, fileFormatType);	
						// 장애 상황 대응 필요 >> status 는 'D'로 변경
						msgbody.put("status", response.get("status"));
						msgbody.put("workId", response.get("workId"));
						msg.put("body", msgbody.toString());					
						InterfaceSolacePub.getInstance().sendBasicTextMessage(cid, msg.toString(), FisPropertyObject.getInstance().getSendTopicName(), fileType);
						// TODO : Work table 상태 (파싱 완료)
						break;
						
					case FisMessageList.FIS_INSP_DATA_SAVE_REP:
					case FisMessageList.FIS_MEAS_DATA_SAVE_REP:
						// TODO : Work table 상태 C (삭제시작)
						String workId = msgbody.getString("workId");
						FisFileMoveExecute fisFileMoveExecute =  ApplicationContextProvider.getBean(FisFileMoveExecute.class);
						fisFileMoveExecute.init();
						fisFileMoveExecute.execute(fileType, fileName, filePath, workId, FisMessageList.FIS_INTF_COMP);
						// TODO : Work table 상태 (삭제 완료)
						break;
					case FisMessageList.FIS_DLT_REQ:
						// TODO : work table 조회 > 'C', 'D' 상태 읽기
						// 'C', 'D' 상태의 workid list 롤 만들기, 
						// DeleteBatch() 하고, 
						// Work History 에 insert > work table 삭제
//						break;
//					case FisMessageList.FIS_INTF_FAIL:	//데이터만 삭제 - BRS에서 입력 실패 파일에 대한 메세지를 송신 해 줄 때 사용 (미정) 
//						String workId = msgbody.getString("workId");
//						FisFileMoveExecute fisFileMoveExecute =  ApplicationContextProvider.getBean(FisFileMoveExecute.class);
//						fisFileMoveExecute.init();
//						fisFileMoveExecute.execute(filePath, fileName, tofilePath, workId);
//						break;
					default:
						log.error("## Invalied cid : "+cid);
						break;
					}

				}
				
				message.ackMessage();
				
			}catch (Exception e){
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
