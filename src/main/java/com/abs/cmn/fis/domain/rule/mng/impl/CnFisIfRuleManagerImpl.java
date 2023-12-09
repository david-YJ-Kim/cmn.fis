package com.abs.cmn.fis.domain.rule.mng.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abs.cmn.fis.config.FisPropertyObject;
import com.abs.cmn.fis.domain.rule.mng.CnFisIfRuleManager;
import com.abs.cmn.fis.domain.rule.model.CnFisIfParseRule;
import com.abs.cmn.fis.domain.rule.model.CnFisIfParseRuleRel;
import com.abs.cmn.fis.domain.rule.service.CnFisIfParseRuleRelService;
import com.abs.cmn.fis.domain.rule.service.CnFisIfParseRuleService;
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
public class CnFisIfRuleManagerImpl implements CnFisIfRuleManager{
	
	@Autowired
	private CnFisIfParseRuleRelService cnFisIfParseRuleRelService;

	@Autowired
	private CnFisIfParseRuleService cnFisIfParseRuleService;

	@Override
	public void init() {
		cnFisIfParseRuleRelService = ApplicationContextProvider.getBean(CnFisIfParseRuleRelService.class);
		cnFisIfParseRuleService = ApplicationContextProvider.getBean(CnFisIfParseRuleService.class);
	}
	
	@Override
	public boolean reloadBaseRuleData() {
    	try {
    		Map<String, ParseRuleVo> ruleVoMap = FisPropertyObject.getInstance().getNextRuleVoMap();
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
	
	// 준비된 기준정보를 적용 
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
	
	@Override
	public void initializeRuleData(){
 
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
 
        log.info("기준정보 로딩 완료. Rule Info: {}",
                FisPropertyObject.getInstance().getNextRuleVoMap().toString());
 
    }
 
	@Override
	public ParseRuleVo setParsingRuleVo(CnFisIfParseRule ruleEntity, List<CnFisIfParseRuleRel> relations){
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
 
}
