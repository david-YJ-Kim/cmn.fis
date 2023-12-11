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
//        boolean stopReceiving;
//        try {
//            stopReceiving = FisPropertyObject.getInstance().getInterfaceSolaceSub().stopQueueReceiver();
//        } catch (JCSMPInterruptedException e) {
//            e.printStackTrace();
//            stopReceiving = false;
////            throw new RuntimeException(e);
//        }
//        log.info("Is Flow Receiver closed ? :{}", stopReceiving);
//
//
//
//
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
//
//
//
//
//    }
//}
