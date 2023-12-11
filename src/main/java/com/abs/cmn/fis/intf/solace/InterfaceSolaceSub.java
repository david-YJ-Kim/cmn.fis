package com.abs.cmn.fis.intf.solace;

import com.abs.cmn.fis.config.FisPropertyObject;
import com.abs.cmn.fis.config.SolaceSessionConfiguration;
import com.abs.cmn.fis.intf.solace.broker.Receiver;
import com.abs.cmn.fis.util.code.FisConstant;
import com.solacesystems.jcsmp.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.*;

@Slf4j
//@Service
public class InterfaceSolaceSub {


//    @Autowired
//    TaskExecutor executor;
	private JCSMPSession session;
    private Receiver receiver;

    private ExecutorService executorService;
    private Future<String> future;


//    private static final String SAMPLE_NAME = InterfaceSolaceSub.class.getSimpleName();
//    private static String QUEUE_NAME = "SVM_DEV_BRS_LOT_00";		// used queue name ex ) PROP_RMP_00
//    private static final String API = "JCSMP";
//    private static volatile int msgRecvCounter = 0;                 // num messages received
//    private static volatile boolean hasDetectedRedelivery = false;  // detected any messages being redelivered?
//    private static volatile boolean isShutdown = false;             // are we done?

    public InterfaceSolaceSub() throws JCSMPException {

        this.executorService = Executors.newCachedThreadPool();


        // init subscribe
//        QUEUE_NAME = queueName;
//        this.subSession = subSession;
//        this.subSession.connect();
//        this.queue = JCSMPFactory.onlyInstance().createQueue(queueName);
//        this.consumerFlowProperties = this.setConsumerFlowProperties();

//		subSession = null;
//		queue = null;
//		consumerFlowProperties = null;
	}

//    @Override
    public void run() {
        try {
            start();
        } catch (JCSMPException | IOException | InterruptedException e) {
            e.printStackTrace();
            log.error(e.toString());

            throw new RuntimeException("Solace receiver fail to start to interface");
        }
    }

    private void start() throws JCSMPException, IOException, InterruptedException {

        try {


			SolaceSessionConfiguration sessionConfiguration = SolaceSessionConfiguration.getSessionConfiguration();
			session = sessionConfiguration.getSession("SUB");
			//session 연결 - Application별로 최소 연결 권장(쓰레드를 사용할 경우 공유 사용 권장)
			session.connect();

			FisPropertyObject fisPropertyObject = FisPropertyObject.getInstance();

			String threadName = fisPropertyObject.getClientName() + "-" + FisConstant.receiver.name();
			String receiveQueueName = fisPropertyObject.getReceiveQueueName();

			this.receiver = new Receiver(session, threadName, receiveQueueName);

//            Receiver receiver = new Receiver(session, threadName, receiveQueueName);
//            this.receiver = receiver;
//            Future<String> future = this.executorService.submit(receiver);
//            this.future = this.executorService.submit(receiver);



//            System.out.println("result : " + future.get());

			Thread thread = new Thread(this.receiver);
//            executor.execute(receiver);

			thread.start();

        } catch (OperationNotSupportedException | JCSMPErrorResponseException e) {  // not allowed to do this
            e.printStackTrace();
			log.error(e.toString());
            throw new RuntimeException("Solace receiver fail to start to interface");
        }
//        catch (ExecutionException e) {
//            throw new RuntimeException(e);
//        }
//        catch (ExecutionException e) {
//            throw new RuntimeException(e);
//        }
    }

    // ##################################################

    public boolean stopQueueReceiver() throws JCSMPInterruptedException {
//        this.session.closeSession();
        boolean stopResult = this.receiver.stopReceiver();
//        this.shutdownAndAwaitTermination(this.executorService);
//        log.info(String.valueOf(stopResult));
//        return stopResult;
        return true;
    }


    @PreDestroy
    public void onDestroy(){

//        log.info("Future is done: {}", this.future.isDone());
//
//
//        while (this.future.isDone()){
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            log.info("Future is done: {}", this.future.isDone());
//            this.executorService.shutdown();
//        }

//        this.executorService.shutdown();
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

    public ExecutorService getExecutorService() {
        return executorService;
    }

    void shutdownAndAwaitTermination(ExecutorService pool) {
        log.info("start to shutdown");
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }

//    @Bean
//    public TaskExecutor taskExecutor() {
//        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
//        taskExecutor.setCorePoolSize(2);
//        taskExecutor.setMaxPoolSize(2);
//        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
//        taskExecutor.initialize();
//        return taskExecutor;
//    }
}
