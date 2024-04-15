package com.abs.cmn.fis.message.initialize;

import com.abs.cmn.fis.domain.rule.mng.FisRuleManager;
import com.abs.cmn.fis.util.code.FisConstant;
import com.solacesystems.jcsmp.BytesXMLMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DataInitializeExecute {

    @Autowired
    FisRuleManager fisRuleManager;

    public void execute(BytesXMLMessage message, String cid){
        this.fisRuleManager.init();

        if (cid.equals(FisConstant.RELOAD_RULE.name()) ) {
            fisRuleManager.reloadBaseRuleData();
        }else if (cid.equals(FisConstant.PATCH_RULE.name()) )
            fisRuleManager.applicationNewBaseRulse();
        else{

            log.error("## Receiver , onReceive() - Invalied Message ! check Messages : " + message.dump() );
        }

    }

}
