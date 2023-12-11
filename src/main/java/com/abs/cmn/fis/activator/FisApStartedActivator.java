package com.abs.cmn.fis.activator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.abs.cmn.fis.domain.rule.service.CnFisIfParseRuleRelService;
import com.abs.cmn.fis.domain.rule.service.CnFisIfParseRuleService;
import com.abs.cmn.fis.message.FisMessagePool;
import com.abs.cmn.fis.util.code.FisConstant;
import com.abs.cmn.fis.util.code.FisQueryValues;
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



//    @Autowired
//    private InterfaceSolaceSub interfaceSolaceSub;

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

        try{

            this.initializeRuleData();
            log.info("Complete initialize rule data.");
        }catch (Exception e){
            e.printStackTrace();
        }

        this.initializeSolaceResources();
        log.info("Complete initialize solace resources.");

        FisMessagePool.getMessageManageMap();
        log.info("Initialize Message Pool. is null?: {}", FisMessagePool.getMessageManageMap() == null);


    }


    private void initializeRuleData(){

        Map<String, ParseRuleVo> ruleVoMap = FisPropertyObject.getInstance().getRuleVoMap();
        log.info("Map is null ? : {}", ruleVoMap == null);

        // 기준정보 (CN_FIS_PARSE_RULE)
        List<CnFisIfParseRule> cnFisIfParseRules = this.cnFisIfParseRuleService.getAllEntities();
        log.info("Get parsing rules : {}", cnFisIfParseRules);

        // 매핑 정보 (CN_FIS_PARSE_RULE_REL)
        List<CnFisIfParseRuleRel> cnFisIfParseRuleRelations = this.cnFisIfParseRuleRelService.getAllEntities();
        log.info("Get rule relation infos: {}", cnFisIfParseRuleRelations);



        for(CnFisIfParseRule entity : cnFisIfParseRules){
            String mapKey = FisCommonUtil.generateRuleStoreKey(entity.getEqpId(), entity.getFileTyp().name());
            ruleVoMap.put(mapKey, this.setParsingRuleVo(entity, cnFisIfParseRuleRelations));
        }


//        List<ParseRuleRelVo> relationInfoVos = FisCommonUtil.convertParseRuleRelVo(cnFisIfParseRuleRelations);
//        FisPropertyObject.getInstance().setMappingRule(relationInfoVos);
//
//        // CnFisIfParsingFileInfo > VO 명을 현행화 CnFisIfParseRule
//        List<ParseRuleVo> parsingInfoVos = FisCommonUtil.convertParseRuleVo(cnFisIfParseRules, relationInfoVos);
//        FisPropertyObject.getInstance().setParsingRule(parsingInfoVos);

        log.info("기준정보 로딩 완료. Rule Info: {}",
                FisPropertyObject.getInstance().getRuleVoMap().toString());

    }

    private ParseRuleVo setParsingRuleVo(CnFisIfParseRule ruleEntity, List<CnFisIfParseRuleRel> relations){
        ParseRuleVo parseRuleVo = new ParseRuleVo();
        String objId = ruleEntity.getObjId();

        /**
         * 기초 변수 설정
         */
        parseRuleVo.setObjId(ruleEntity.getObjId());
        parseRuleVo.setEqpId(ruleEntity.getEqpId());
        parseRuleVo.setFileTyp(ruleEntity.getFileTyp());
        parseRuleVo.setStartHdrVal(ruleEntity.getStartHdrVal());
        parseRuleVo.setFileTgtPosnVal(ruleEntity.getFileTgtPosnVal());
        parseRuleVo.setParsClmIdVal(ruleEntity.getParsClmIdVal());
        parseRuleVo.setParseRowVal(ruleEntity.getParsRowVal());

        /**
         * Relation Rule 설정
         */
        ArrayList<ParseRuleRelVo> relatedRule = new ArrayList<>();
        for(CnFisIfParseRuleRel relation : relations){
            if(objId.equals(relation.getRefObjId())){

                relatedRule.add(ParseRuleRelVo.builder()
                                .objId(relation.getObjId())
                                .refObjId(relation.getRefObjId())
                                .fileClmVal(relation.getFileClmVal())
                                .fileClmNumIntVal(FisCommonUtil.changeClmTitlVal(relation.getFileClmVal()))
                                .mpngClmNm(relation.getMpngClmNm())
                                .clmDataTyp(relation.getClmDataTyp())
                                .build());
            }
        }
        parseRuleVo.setRelationVoList(relatedRule);


        /**
         * 확장 아이템 설정
         */
        String query = FisCommonUtil.makeBatchInsertQuery(ruleEntity.getFileTyp().name(), objId, relatedRule);
        parseRuleVo.setQueryInsertBatch(query);
        parseRuleVo.setMpngClmStrList(FisCommonUtil.getMappingColumns(String.valueOf(objId), relatedRule));
        parseRuleVo.setNumberDataTypList(FisCommonUtil.getDataTypeList(query, FisQueryValues.NUMBER.name(), relatedRule));
        parseRuleVo.setTimeStampDataTypList(FisCommonUtil.getDataTypeList(query, FisQueryValues.TIMESTAMP.name(), relatedRule));

        return parseRuleVo;
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

//            this.interfaceSolaceSub.run();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
