package com.abs.cmn.fis.activator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.abs.cmn.fis.config.FisPropertyObject;
import com.abs.cmn.fis.config.SolaceSessionConfiguration;
import com.abs.cmn.fis.domain.rule.mng.CnFisIfRuleManager;
import com.abs.cmn.fis.intf.solace.InterfaceSolacePub;
import com.abs.cmn.fis.intf.solace.InterfaceSolaceSub;
import com.abs.cmn.fis.message.FisMessagePool;
import com.solacesystems.jcsmp.JCSMPException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FisApStartedActivator implements ApplicationRunner {

    @Autowired
    private Environment env;

    @Autowired
    private CnFisIfRuleManager cnFisIfRuleManager;

    @Override
    public void run(ApplicationArguments args){

        try{

//        	cnFisIfRuleManager.init();
//        	cnFisIfRuleManager.initializeRuleData();
            log.info("Complete initialize rule data.");
        }catch (Exception e){
            e.printStackTrace();
        }

        this.initializeSolaceResources();
        log.info("Complete initialize solace resources.");

        FisMessagePool.getMessageManageMap();
        log.info("Initialize Message Pool. is null?: {}", FisMessagePool.getMessageManageMap() == null);


    }

    private void initializeSolaceResources(){

        SolaceSessionConfiguration sessionConfiguration = SolaceSessionConfiguration.createSessionConfiguration(env);

        try {
            InterfaceSolacePub interfaceSolacePub = InterfaceSolacePub.getInstance();
            interfaceSolacePub.init();
            FisPropertyObject.getInstance().setInterfaceSolacePub(interfaceSolacePub);

        } catch (JCSMPException e) {
            throw new RuntimeException(e);
        }

        try {
            InterfaceSolaceSub interfaceSolaceSub = new InterfaceSolaceSub();
            interfaceSolaceSub.run();
            FisPropertyObject.getInstance().setInterfaceSolaceSub(interfaceSolaceSub);

        } catch (JCSMPException e) {
            throw new RuntimeException(e);
        }

    }
}
