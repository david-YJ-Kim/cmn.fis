package com.abs.cmn.fis.domain.rule.mng.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abs.cmn.fis.config.FisPropertyObject;
import com.abs.cmn.fis.domain.rule.mng.FisRuleManager;
import com.abs.cmn.fis.domain.rule.model.CnFisParseRule;
import com.abs.cmn.fis.domain.rule.model.CnFisParseRuleRel;
import com.abs.cmn.fis.domain.rule.service.CnFisParseRuleRelService;
import com.abs.cmn.fis.domain.rule.service.CnFisParseRuleService;
import com.abs.cmn.fis.util.ApplicationContextProvider;
import com.abs.cmn.fis.util.FisCommonUtil;
import com.abs.cmn.fis.util.code.FisQueryValues;
import com.abs.cmn.fis.util.vo.ParseRuleRelVo;
import com.abs.cmn.fis.util.vo.ParseRuleVo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FisRuleManagerImpl implements FisRuleManager {

    @Autowired
    private CnFisParseRuleRelService cnFisParseRuleRelService;

    @Autowired
    private CnFisParseRuleService cnFisParseRuleService;

    @Override
    public void init() {
        /*
            Bean 생성 이전, Application Runner 에서 호출이 필요한 경우.
            init 메소드를 통해 직접 bean 을 injection 한다.
         */
        cnFisParseRuleRelService = ApplicationContextProvider.getBean(CnFisParseRuleRelService.class);
        cnFisParseRuleService = ApplicationContextProvider.getBean(CnFisParseRuleService.class);
    }

    /**
     * reload base rule data.
     * @return
     */
    @Override
    public boolean reloadBaseRuleData() {
        try {
            Map<String, ParseRuleVo> ruleVoMap = FisPropertyObject.getInstance().getNextRuleVoMap();
            log.info("Map is null ? : {}", ruleVoMap == null);

            // 기준정보 (CN_FIS_PARSE_RULE)
            List<CnFisParseRule> cnFisParseRules = this.cnFisParseRuleService.getAllEntities();
            log.info("Get parsing rules : {}", cnFisParseRules);


            for(CnFisParseRule entity : cnFisParseRules){
                String mapKey = FisCommonUtil.generateRuleStoreKey(entity.getEqpId(), entity.getFileTyp().name());
                ruleVoMap.put(mapKey, this.setParsingRuleVo(entity));
            }

            log.info("기준정보 로딩 완료. Rule Info: {}",
                    FisPropertyObject.getInstance().getRuleVoMap().toString());


            FisPropertyObject.getInstance().setPrevRuleVoMap(
                    FisPropertyObject.getInstance().getRuleVoMap()
            );

            return true;

        } catch (Exception e) {
            log.info("## FisCommonUtil, reloadBaseRuleData ", e);
            return false;
        }

    }


    /**
     * 준비된 기준정보 적용?
     * @return
     */
    @Override
    public boolean applicationNewBaseRulse() {
        try {

            FisPropertyObject.getInstance().setRuleVoMap(
                    FisPropertyObject.getInstance().getNextRuleVoMap()
            );

            return true;
        } catch (Exception e) {
            log.info("## FisCommonUtil, applicationNewBaseRulse ", e);
            return false;
        }

    }

    /**
     * Access data structure generated when application started.
     */
    @Override
    public void initializeRuleData(){

        Map<String, ParseRuleVo> ruleVoMap = FisPropertyObject.getInstance().getRuleVoMap();
        log.info("Map is null ? : {}", ruleVoMap == null);

        // 기준정보 (CN_FIS_PARSE_RULE)
        List<CnFisParseRule> cnFisParseRules = this.cnFisParseRuleService.getAllEntities();
        log.info("Get parsing rules : {}", cnFisParseRules);

        for(CnFisParseRule entity : cnFisParseRules){
            String masterDataKey = FisCommonUtil.generateRuleStoreKey(entity.getEqpId(), entity.getFileTyp().name());
            ruleVoMap.put(masterDataKey, this.setParsingRuleVo(entity));
        }

        log.info("기준정보 로딩 완료. Rule Info: {}", ruleVoMap.toString());

    }

    /**
     * Set rule relation data based on master data (CnFisParseRule)
     * manipulate data with useful way. such as column list into colum index list.
     * @param ruleEntity Master data query result.
     * @return Master data object (a.k.a. ParseRuleVo) including relation data.
     */
    public ParseRuleVo setParsingRuleVo(CnFisParseRule ruleEntity){
        ParseRuleVo parseRuleVo = new ParseRuleVo();
        String objId = ruleEntity.getObjId();

        /*
         * 기초 변수 설정
         */
        parseRuleVo.setObjId(ruleEntity.getObjId());
        parseRuleVo.setEqpId(ruleEntity.getEqpId());
        parseRuleVo.setFileTyp(ruleEntity.getFileTyp());
        parseRuleVo.setFileMoveDirectoryValue(ruleEntity.getFileTgtPosnVal());
        parseRuleVo.setParsClmIdVal(ruleEntity.getParsClmIdVal());
        parseRuleVo.setParseRowVal(ruleEntity.getParsRowVal());

        /*
            Get relation data.
         */
        List<CnFisParseRuleRel> relationEntities =  this.cnFisParseRuleRelService.findCnFisParseRuleRelsByEqpIdAndFileTyp(
                                                        ruleEntity.getEqpId(), ruleEntity.getFileTyp());
        log.info("Query to get relation data. {}", relationEntities.toString());


        /*
         * Relation Rule 설정
         */
        ArrayList<ParseRuleRelVo> relatedRule = new ArrayList<>();
        for(CnFisParseRuleRel relation : relationEntities){

            relatedRule.add(ParseRuleRelVo.builder()
                    .objId(relation.getObjId())
                    .eqpId(relation.getEqpId())
                    .fileTyp(relation.getFileTyp())
                    .fileClmVal(relation.getFileClmVal())
                    .fileClmNumIntVal(FisCommonUtil.changeClmTitlVal(relation.getFileClmVal()))
                    .mpngClmNm(relation.getMpngClmNm())
                    .clmDataTyp(relation.getClmDataTyp())
                    .build());
        }
        parseRuleVo.setRelationVoList(relatedRule);


        /*
         * Manipulation master data into useful way.
         */
        String insertQueryStatement = FisCommonUtil.makeBatchInsertQuery(ruleEntity.getFileTyp(), relatedRule);

        parseRuleVo.setQueryInsertBatch(insertQueryStatement);
        parseRuleVo.setMpngClmStrList(FisCommonUtil.getMappingColumns(String.valueOf(objId), relatedRule));
        parseRuleVo.setNumberDataTypList(FisCommonUtil.getDataTypeList(insertQueryStatement, FisQueryValues.NUMBER.name(), relatedRule));
        parseRuleVo.setTimeStampDataTypList(FisCommonUtil.getDataTypeList(insertQueryStatement, FisQueryValues.TIMESTAMP.name(), relatedRule));

        log.info(parseRuleVo.toString());
        return parseRuleVo;
    }

}
