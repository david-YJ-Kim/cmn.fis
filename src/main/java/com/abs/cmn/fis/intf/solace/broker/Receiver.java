package com.abs.cmn.fis.intf.solace.broker;

import com.abs.cmn.fis.message.initialize.DataInitializeExecute;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.solacesystems.jcsmp.*;
import org.springframework.core.task.TaskRejectedException;

import com.abs.cmn.fis.config.FisSharedInstance;
import com.abs.cmn.fis.domain.work.controller.FisWorkTableManageController;
import com.abs.cmn.fis.message.FisMessagePool;
import com.abs.cmn.fis.message.parse.FisFileParsingExecute;
import com.abs.cmn.fis.message.vo.receive.FisFileReportVo;
import com.abs.cmn.fis.message.vo.receive.FisTestMessageVo;
import com.abs.cmn.fis.util.ApplicationContextProvider;
import com.abs.cmn.fis.util.FisMessageList;
import com.abs.cmn.fis.util.code.FisConstant;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Receiver implements Runnable {


    private JCSMPSession session;
    private EndpointProperties endPointProps;
    private FlowReceiver consumer;
    private String thread_name;
    private String queue_name;

    private boolean stopFlagOn = false;


    public Receiver(JCSMPSession session, String thread_name, String queue_name) {
        this.session = session;
        this.queue_name = queue_name;
        this.thread_name = thread_name;
    }

    @SneakyThrows
    @Override
    public void run() {
        try {

            log.info("Interface receiver thread {}, now start to receive message from {}.", this.thread_name, this.queue_name);
            // Queue - SolAdmin에서 생성한 queue에 접속, SolAdmin에 생성되지 않은 경우 Application 에서 생성
            final Queue queue = JCSMPFactory.onlyInstance().createQueue(queue_name);

            // Way to mapping topic to queue
            // Topic topic = JCSMPFactory.onlyInstance().createTopic("Queue/Name/into/Topic/Name");
            // this.session.addSubscription(queue, topic, JCSMPSession.WAIT_FOR_CONFIRM);

            /* ConsumerFlow 설정 */
            final ConsumerFlowProperties flowProps = new ConsumerFlowProperties();
            // Set flow to connect with queue.
            flowProps.setEndpoint(queue);
            // Manual Ack 설정
            flowProps.setAckMode(JCSMPProperties.SUPPORTED_MESSAGE_ACK_CLIENT);

            // FlowReceiver 생성
            consumer = session.createFlow(new MessageListener(this), flowProps, endPointProps);
            // FlowReceiver 실행(start를 해야 Endpoint로부터 메시지를 수신할 수 있음)
            consumer.start();

        } catch (JCSMPException e) {
            log.error(e.getMessage());
            throw new Exception(e);
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

        this.switchStopFlag();
        this.consumer.stop();
        log.info("Consumer Stop!!");
        return true;
    }

    /**
     * Message listen call back class.
     * After establish connection and new message comes in, this class and method will be triggered.
     */
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

            // set for unknown property.
            ObjectMapper mapper = new ObjectMapper().configure(
                    DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

            String endpointName = message.getDestination().getName();

            SDTMap userProperty = message.getProperties();
            String cid = userProperty.getString(FisConstant.cid.name());
            String key = userProperty.getString(FisConstant.messageId.name());
            String trackingKey = FisMessagePool.putMessageObject(key, message);
            log.info("In-coming new message. from {}. Get tracking info from message. " +
                            "event name(cid): {}, message key: {}, trackingKey: {}." +
                            "Starting manage in map: {}. It's size: {}",
                    endpointName, cid, key, trackingKey,
                    FisMessagePool.getMessageManageMap().toString(), FisMessagePool.getMessageManageMap().size());

            try{

                if (message.getDestination().equals(FisSharedInstance.getInstance().getReceiveInitTopic())) {
                    DataInitializeExecute dataInitializeExecute = ApplicationContextProvider.getBean(
                            DataInitializeExecute.class);
                    dataInitializeExecute.execute(message, cid);
                    log.info("{} Complete data initialize.", trackingKey);
                    return;
                }

                String payload = "";
                if ( message instanceof TextMessage) {
                    payload = ((TextMessage) message).getText();
                } else {
                    payload = new String( message.getBytes(), "UTF-8");
                }


                log.info("{} Incoming event-name(cid):{}"
                        , trackingKey, cid);
                switch (cid){

                    /**
                     * Async 처리로 Message Ack는 execute 내부에서 처리
                     * File report message sequence.
                     * 1. Status `R` : Receive message and create work stats
                     * 2. Status `P` : Start read and parsing target file.
                     * 3. Status `I` : Complete parsing and start insert data.
                     * 4. Status `C` : Complete FIS work.
                     * 5. Status `E` : Error while FIS work.
                     * 6. Status `S` : Complete to send message.
                     */
                    case FisMessageList.FIS_FILE_REPORT:	// 파일 파싱 , 워크 생성 - R, 파일 저장,

                        FisFileReportVo fisFileReportVo = mapper.readValue(payload, FisFileReportVo.class);

                        FisFileParsingExecute fisFileParsingExecute = ApplicationContextProvider.getBean(
                                FisFileParsingExecute.class);
                        fisFileParsingExecute.init();

                        try {
                            fisFileParsingExecute.execute(fisFileReportVo, trackingKey);
                            // → Async 메소드, 메소드 외부에서 Ack 처리 및 결과 출력 시, Null

                        }catch (Exception e){
                            log.error("{} Exception : {}", trackingKey, e);
                            FisMessagePool.messageAck(trackingKey);
                            e.printStackTrace();
                            throw e;
                        }

                        break;


                    case FisMessageList.FIS_DLT_REQ:	// D 인 데이터 값 찾아서 History 로 적재 & 해당 ObjID 데이터 삭제
                        FisWorkTableManageController workctlr = ApplicationContextProvider.getBean(
                                FisWorkTableManageController.class);

                        try{

                            // TODO Make it Async
                            workctlr.startDeleteLogic();

                            // → Ack logic need to merge in delete logic.
                            FisMessagePool.messageAck(trackingKey);
                            log.info("Message has been acked.");


                        }catch (Exception e){
                            e.printStackTrace();
                            throw e;
                        }
                        break;


                    case FisMessageList.FIS_TEST_MESSAGE:
                        FisTestMessageVo fisTestMessageVo = mapper.readValue(payload, FisTestMessageVo.class);
                        log.info("[{}] Request vo: {}", trackingKey, fisTestMessageVo.getBody().toString());

                        try{
                            Thread.sleep(fisTestMessageVo.getBody().getSleepTm());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        FisMessagePool.messageAck(trackingKey);
                        log.info("Message has been acked.");

                        break;

                    default:
                        log.error("## Invalied cid : "+cid);

                        break;
                }


            }catch(TaskRejectedException taskRejectedException){
                taskRejectedException.printStackTrace();
                log.error("Over capacity. It's overflow. Error: {}", taskRejectedException);
                FisMessagePool.messageAck(trackingKey);


            }catch (Exception e){
                log.error("##  Receiver.onReceive() Exception : {}", e);
                FisMessagePool.messageAck(trackingKey);

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
