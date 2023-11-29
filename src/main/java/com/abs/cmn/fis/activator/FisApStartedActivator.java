package com.abs.cmn.fis.activator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.abs.cmn.fis.config.FisPropertyObject;
import com.abs.cmn.fis.config.SolaceSessionConfiguration;
import com.abs.cmn.fis.domain.rule.model.CnFisIfParseRule;
import com.abs.cmn.fis.domain.rule.model.CnFisIfParseRuleRel;
import com.abs.cmn.fis.domain.rule.service.CnFisIfParsingDataMappingInfoService;
import com.abs.cmn.fis.domain.rule.service.CnFisIfParsingFileInfoService;
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
    private CnFisIfParsingDataMappingInfoService cnFisIfParsingDataMappingInfoService;

    @Autowired
    private CnFisIfParsingFileInfoService cnFisIfParsingFileInfoService;

    @Override
    public void run(ApplicationArguments args){
        
        // TODO 기준정보 초기화 >> VO명칭 변경  CnFisIfParsingDataMappingInfo >  CnFisIfParseRuleRel
//    	 rule Vo 와 Mapping Vo 를 나누어 1 : N 구조롤 나눈다. 
        List<CnFisIfParseRuleRel> mappingInfoEntities = this.cnFisIfParsingDataMappingInfoService.getAllEntities();
        List<ParseRuleRelVo> mappingInfoVos = FisCommonUtil.convertMappingInfoInfoVo(mappingInfoEntities);
        FisPropertyObject.getInstance().setMappingRule(mappingInfoVos);

        // CnFisIfParsingFileInfo > VO 명을 현행화 CnFisIfParseRule
        List<CnFisIfParseRule> parsingInfoEntities = this.cnFisIfParsingFileInfoService.getAllEntities();
        List<ParseRuleVo> parsingInfoVos = FisCommonUtil.convertParsingInfoVo(parsingInfoEntities, mappingInfoVos);
        FisPropertyObject.getInstance().setParsingRule(parsingInfoVos);

        log.info("기준정보 로딩 완료. MappingInfos: {}, ParsingInfo: {}",
                FisPropertyObject.getInstance().getMappingRule().toString(), FisPropertyObject.getInstance().getParsingRule().toString());



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
