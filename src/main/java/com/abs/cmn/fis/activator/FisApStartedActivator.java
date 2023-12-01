package com.abs.cmn.fis.activator;

import java.util.List;

import com.abs.cmn.fis.domain.rule.service.CnFisIfParseRuleRelService;
import com.abs.cmn.fis.domain.rule.service.CnFisIfParseRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.abs.cmn.fis.config.FisPropertyObject;
import com.abs.cmn.fis.config.SolaceSessionConfiguration;
import com.abs.cmn.fis.domain.rule.model.CnFisIfParseRule;
import com.abs.cmn.fis.domain.rule.model.CnFisIfParseRuleRel;
import com.abs.cmn.fis.domain.work.service.CnFisWorkService;
import com.abs.cmn.fis.intf.solace.InterfaceSolacePub;
import com.abs.cmn.fis.intf.solace.InterfaceSolaceSub;
import com.abs.cmn.fis.util.FisCommonUtil;
import com.abs.cmn.fis.util.vo.ParseRuleRelVo;
import com.abs.cmn.fis.util.vo.ParseRuleVo;
import com.solacesystems.jcsmp.JCSMPException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FisApStartedActivator implements ApplicationRunner {


    @Autowired
    private Environment env;

    @Autowired
    private CnFisWorkService workService;

    @Autowired
    private CnFisIfParseRuleRelService cnFisIfParseRuleRelService;

    @Autowired
    private CnFisIfParseRuleService cnFisIfParseRuleService;

    @Override
    public void run(ApplicationArguments args){
        
        this.initializeRuleData();
        log.info("Complete initialize rule data.");

        this.initializeSolaceResources();
        log.info("Complete initialize solace resources.");


    }


    private void initializeRuleData(){

        // 기준정보 (CN_FIS_PARSE_RULE)
        List<CnFisIfParseRule> cnFisIfParseRules = this.cnFisIfParseRuleService.getAllEntities();
        log.info("Get parsing rules : {}", cnFisIfParseRules);

        // 매핑 정보 (CN_FIS_PARSE_RULE_REL)
        List<CnFisIfParseRuleRel> cnFisIfParseRuleRelations = this.cnFisIfParseRuleRelService.getAllEntities();
        log.info("Get rule relation infos: {}", cnFisIfParseRuleRelations);


        List<ParseRuleRelVo> relationInfoVos = FisCommonUtil.convertParseRuleRelVo(cnFisIfParseRuleRelations);
        FisPropertyObject.getInstance().setMappingRule(relationInfoVos);

        // CnFisIfParsingFileInfo > VO 명을 현행화 CnFisIfParseRule
        List<ParseRuleVo> parsingInfoVos = FisCommonUtil.convertParseRuleVo(cnFisIfParseRules, relationInfoVos);
        FisPropertyObject.getInstance().setParsingRule(parsingInfoVos);

        log.info("기준정보 로딩 완료. MappingInfos: {}, ParsingInfo: {}",
                FisPropertyObject.getInstance().getMappingRule().toString(), FisPropertyObject.getInstance().getParsingRule().toString());

//        System.exit(0);
    }

    private void initializeSolaceResources(){

        SolaceSessionConfiguration sessionConfiguration = SolaceSessionConfiguration.createSessionConfiguration(env);

        try {
            InterfaceSolacePub interfaceSolacePub = InterfaceSolacePub.getInstance();
            interfaceSolacePub.init();
        } catch (JCSMPException e) {
            throw new RuntimeException(e);
        }

        try {
            InterfaceSolaceSub interfaceSolaceSub = new InterfaceSolaceSub();
            interfaceSolaceSub.run();
        } catch (JCSMPException e) {
            throw new RuntimeException(e);
        }

    }
}
