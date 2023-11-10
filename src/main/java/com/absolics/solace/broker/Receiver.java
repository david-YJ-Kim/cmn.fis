package com.absolics.solace.broker;

import com.absolics.service.FileReadExcutor;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.solacesystems.jcsmp.TextMessage;
import com.solacesystems.jcsmp.XMLMessageListener;

public class Receiver implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(Receiver.class);
	
    private JCSMPSession session;
    private Queue queue;
    private EndpointProperties endPointProps;
    private FlowReceiver consumer;
    private String module_name;
	private String thread_name;
	private String queue_name;
	
	// FileReadExcutor
	private FileReadExcutor fileReadExcutor = new FileReadExcutor();

    public Receiver(JCSMPSession session, String queueName, String module_name, String thread_name) {    	
    	this.session = session;
    	this.queue_name = queueName;
    	this.module_name = module_name;
    	this.thread_name = thread_name;
    }
    
	@Override
	public void run() {
		try {
			log.info("Receiver Thread Start # "+this.thread_name);
			
			//Queue - SolAdmin에서 생성한 queue에 접속, SolAdmin에 생성되지 않은 경우 Application에서 생성
			queue = JCSMPFactory.onlyInstance().createQueue(queue_name);			
			/* 
			 * EndPoint 설정
			 * - SolAdmin에서 설정이 되어 있는 경우 Applicaiton에서는 사용하지 않아도 됨(사용할 경우 SolAdmin 화면과 동일하게 구성)
			 * - SolAdmin에 설정이 없는 경우 Application에서 설정한 값으로 설정됨             
			 */
			endPointProps = new EndpointProperties();
			/* Endpoint(queue, topic) 설정 - solAdmin 화면에서 설정한 값과 동일해야 함 */
			//Endpoint(Queue) 권한 설정
			endPointProps.setPermission(EndpointProperties.PERMISSION_CONSUME);
	        //Endpoint(Queue) accesstype 설정
			endPointProps.setAccessType(EndpointProperties.ACCESSTYPE_NONEXCLUSIVE);
	        //Endpoint provisioning - solAdmin 에 생성된 Endpoint 가 있으므로 "FLAG_IGNORE_ALREADY_EXISTS" 사용)
//	        session.provision(queue, endPointProps, JCSMPSession.FLAG_IGNORE_ALREADY_EXISTS);

			/* ConsumerFlow 설정 */
	        final ConsumerFlowProperties flowProps = new ConsumerFlowProperties();
	        //Queue에 연결할 flow 설정
	        flowProps.setEndpoint(queue);
	        //Manual Ack 설정
	        flowProps.setAckMode(JCSMPProperties.SUPPORTED_MESSAGE_ACK_CLIENT);

	        //FlowReceiver 생성
	        consumer = session.createFlow(new MessageListener(this), flowProps, endPointProps);
	        //FlowReceiver 실행(start를 해야 Endpoint로부터 메시지를 수신할 수 있음)
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

        @Override
        public void onReceive(BytesXMLMessage message) {
        	try {
        		
//        		log.info("dump: \n{}", message.dump());
				
				// TODO CID로 분기 처리
        		
				if (message instanceof TextMessage) {
					
					log.info("messageId: {}", message.getMessageId()); // Solace message 내장 property(messageId)
					log.info("message body: {}", ((TextMessage) message).getText());
					
					// FileReadExcutor 구동
					fileReadExcutor.fileParsingStart(new JSONObject(((TextMessage) message).getText()));
					
				} else {
										
					log.info("messageId: {}", message.getMessageId());
					log.info("destination: {}", message.getDestination());

					byte[] byteMsg = message.getBytes();
					String str = new String(byteMsg);

					log.info("message body: {}", str.toString());
					
					fileReadExcutor.fileParsingStart(new JSONObject(str));
				}

				message.ackMessage(); // manual ack mode 일 경우 명시적 추가
			} catch (Exception e) {
				log.warn(e.toString());
			}
        }
		
        @Override
        public void onException(JCSMPException exception) {
        	
        	if (consumer.isClosed()) {
				try {
					consumer.start();
				} catch (JCSMPException e) {
					// TODO Auto-generated catch block
					log.error("## Error : Closed Consumer > reStart Consume" , e);
				}
        	} else if (session.isClosed()) {
        		try {
        			session.connect();
				} catch (JCSMPException e) {
					// TODO Auto-generated catch block
					log.error("## Error : Closed Session > reConnecte session" , e);
				}
        	} else {
        		log.error("## Solace Sub onException : " , exception);
        	}
        	
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'onException'");
        }
    }
}
