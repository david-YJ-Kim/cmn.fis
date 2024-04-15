<<<<<<< HEAD
package com.abs.cmn.fis.activator;

<<<<<<< HEAD
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import com.abs.cmn.fis.config.FisPropertyObject;
import com.abs.cmn.fis.message.FisMessagePool;
import com.solacesystems.jcsmp.JCSMPInterruptedException;

import lombok.extern.slf4j.Slf4j;
=======
import com.abs.cmn.fis.config.FisPropertyObject;
import com.abs.cmn.fis.config.SolaceSessionConfiguration;
import com.abs.cmn.fis.intf.solace.InterfaceSolaceSub;
import com.abs.cmn.fis.message.FisMessagePool;
import com.solacesystems.jcsmp.JCSMPInterruptedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
>>>>>>> failover

@Slf4j
@Component
public class FisApStoppedActivator implements ApplicationListener<ContextClosedEvent> {

//    @Autowired
//    private TaskExecutor executor;


//    @Autowired
//    InterfaceSolaceSub interfaceSolaceSub;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.warn("JVM will be stop in sec");
        try {
            FisPropertyObject.getInstance().getInterfaceSolaceSub().stopQueueReceiver();
//            this.interfaceSolaceSub.stopQueueReceiver();
        } catch (JCSMPInterruptedException e) {
            throw new RuntimeException(e);
        }


//        this.interfaceSolaceSub.onDestroy();


//        try {
//            FisPropertyObject.getInstance().getInterfaceSolaceSub().getExecutorService().awaitTermination(10, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

//        try {
//            executor.wait();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//
//            log.info(e.getMessage());
//        }

=======
//package com.abs.cmn.fis.activator;
//
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextClosedEvent;
//import org.springframework.stereotype.Component;
//
//import com.abs.cmn.fis.config.FisPropertyObject;
//import com.abs.cmn.fis.message.FisMessagePool;
//import com.solacesystems.jcsmp.JCSMPInterruptedException;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Component
//public class FisApStoppedActivator implements ApplicationListener<ContextClosedEvent> {
//    @Override
//    public void onApplicationEvent(ContextClosedEvent event) {
//        log.warn("JVM will be stop in sec");
//
>>>>>>> fa-threadJoin
//        boolean stopReceiving;
//        try {
//            stopReceiving = FisPropertyObject.getInstance().getInterfaceSolaceSub().stopQueueReceiver();
//        } catch (JCSMPInterruptedException e) {
//            e.printStackTrace();
//            stopReceiving = false;
////            throw new RuntimeException(e);
//        }
//        log.info("Is Flow Receiver closed ? :{}", stopReceiving);
<<<<<<< HEAD

//        ExecutorService executorService = FisPropertyObject.getInstance().getInterfaceSolaceSub().getExecutorService();

//        log.info("Start to termination");
//        try {
//            FisPropertyObject.getInstance().getInterfaceSolaceSub().stopQueueReceiver();
//        } catch (JCSMPInterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        try {
//            this.shutdownAndAwaitTermination(executorService);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        log.info("End termination");


=======
//
//
//
//
>>>>>>> fa-threadJoin
//        int cnt = 0;
//        int maxCount = FisPropertyObject.getInstance().getApShutdownForceTimeoutMs() / FisPropertyObject.getInstance().getApShutdownPollingIntervalMs();
//        while (true){
//            int remainedMessageSizeInStore = FisMessagePool.getMessageManageMap().size();
//
//
//            log.debug(
//                    String.valueOf(remainedMessageSizeInStore)
//            );
//            cnt++;
//
//            if(remainedMessageSizeInStore == 0){
//                log.info("All message has been cleared.! size: {}", remainedMessageSizeInStore);
//
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//
//                log.warn("World is Shutdown.!!");
//                break;
//            }
//
//
//            if(cnt > maxCount){
//                log.info("Shutdown timeout. meet the limit.!");
//
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//
//
//                log.warn("World is Shutdown.!!");
//
//
//
//                break;
//            }
//
//            try {
//                Thread.sleep(FisPropertyObject.getInstance().getApShutdownPollingIntervalMs());
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
<<<<<<< HEAD





    }

//    void shutdownAndAwaitTermination(ExecutorService pool) throws InterruptedException {
//
//
//
//        int cnt = 0;
//        while (true){
//
//            log.info("Pool Is terminated? :{} ", pool.isShutdown());
//
//            pool.awaitTermination(100, TimeUnit.MILLISECONDS);
//
//            if(100 < ++cnt){
//                pool.shutdownNow();
//                log.info("Timeout");
//                break;
//            }
//
//
//            if(pool.isTerminated()){
//                break;
//            }
//        }


//        pool.shutdown(); // Disable new tasks from being submitted
//        try {
//            // Wait a while for existing tasks to terminate
//            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
//                pool.shutdownNow(); // Cancel currently executing tasks
//                // Wait a while for tasks to respond to being cancelled
//                if (!pool.awaitTermination(60, TimeUnit.SECONDS))
//                    System.err.println("Pool did not terminate");
//            }
//        } catch (InterruptedException ie) {
//            // (Re-)Cancel if current thread also interrupted
//            pool.shutdownNow();
//            // Preserve interrupt status
//        }
//        Thread.currentThread().interrupt();
//    }
}
=======
//
//
//
//
//    }
//}
>>>>>>> fa-threadJoin
