package com.abs.cmn.fis.activator;

import com.abs.cmn.seq.SequenceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.abs.cmn.fis.config.FisSharedInstance;
import com.abs.cmn.fis.config.SolaceSessionConfiguration;
import com.abs.cmn.fis.domain.rule.mng.FisRuleManager;
import com.abs.cmn.fis.intf.solace.InterfaceSolacePub;
import com.abs.cmn.fis.intf.solace.InterfaceSolaceSub;
import com.abs.cmn.fis.message.FisMessagePool;
import com.solacesystems.jcsmp.JCSMPException;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@Component
public class FisApStartedActivator implements ApplicationRunner {

    @Autowired
    private Environment env;

    @Autowired
    private FisRuleManager fisRuleManager;

    @Override
    public void run(ApplicationArguments args){

        try{

            fisRuleManager.init();
            fisRuleManager.initializeRuleData();
            log.info("Complete initialize rule data.");
        }catch (Exception e){
            e.printStackTrace();
        }


        try {
            this.initializeSequenceManager();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.initializeSolaceResources();
        log.info("Complete initialize solace resources.");

        FisMessagePool.getMessageManageMap();
        log.info("Initialize Message Pool. is null?: {}", FisMessagePool.getMessageManageMap() == null);


    }

    private void initializeSequenceManager() throws IOException {
        SequenceManager sequenceManager = new SequenceManager(
                FisSharedInstance.getInstance().getGroupName(),
                FisSharedInstance.getInstance().getSiteName(),
                FisSharedInstance.getInstance().getEnvType(),
                FisSharedInstance.getInstance().getSeqRulePath(),
                FisSharedInstance.getInstance().getSeqRuleName()
        );

        FisSharedInstance.getInstance().setSequenceManager(sequenceManager);
    }

    private void initializeSolaceResources(){

        SolaceSessionConfiguration sessionConfiguration = SolaceSessionConfiguration.createSessionConfiguration(env);

        try {
            InterfaceSolacePub interfaceSolacePub = InterfaceSolacePub.getInstance();
            interfaceSolacePub.init();
            FisSharedInstance.getInstance().setInterfaceSolacePub(interfaceSolacePub);

        } catch (JCSMPException e) {
            throw new RuntimeException(e);
        }

        try {
            InterfaceSolaceSub interfaceSolaceSub = new InterfaceSolaceSub();
            interfaceSolaceSub.run();
            FisSharedInstance.getInstance().setInterfaceSolaceSub(interfaceSolaceSub);

        } catch (JCSMPException e) {
            throw new RuntimeException(e);
        }

    }
}
