package com.abs.cmn.fis.domain.rule.mng.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.abs.cmn.fis.config.FisPropertyObject;
import com.abs.cmn.fis.domain.rule.model.CnFisIfParseRule;
import com.abs.cmn.fis.domain.rule.model.CnFisIfParseRuleRel;
import com.abs.cmn.fis.domain.rule.service.CnFisIfParseRuleRelService;
import com.abs.cmn.fis.domain.rule.service.CnFisIfParseRuleService;
import com.abs.cmn.fis.util.FisCommonUtil;
import com.abs.cmn.fis.util.vo.ParseRuleRelVo;
import com.abs.cmn.fis.util.vo.ParseRuleVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CnFisIfRuleManagerImpl {
	
	@Autowired
	private static CnFisIfParseRuleRelService cnFisIfParseRuleRelService;

	@Autowired
	private static CnFisIfParseRuleService cnFisIfParseRuleService;

	
	public boolean reloadBaseRuleData() {
    	try {
	    	// DB에서 기준 정보 읽어옴
	    	List<CnFisIfParseRuleRel> mappingInfoEntities = cnFisIfParseRuleRelService.getAllEntities();
	        List<ParseRuleRelVo> mappingInfoVos = FisCommonUtil.convertParseRuleRelVo(mappingInfoEntities);
	        FisPropertyObject.getInstance().setPrepMappingRule(mappingInfoVos);
	
	        List<CnFisIfParseRule> parsingInfoEntities = cnFisIfParseRuleService.getAllEntities();
//	        List<ParseRuleVo> parsingInfoVos = FisCommonUtil.convertParseRuleVo(parsingInfoEntities, mappingInfoVos);
			List<ParseRuleVo> parsingInfoVos = null;
	        FisPropertyObject.getInstance().setPrepParsingRule(parsingInfoVos);
	        
	        // 현재 운영 룰 past에 저장 해 놓음
//	        FisPropertyObject.getInstance().setPastMappingRule(
//	        		FisPropertyObject.getInstance().getMappingRule()
//	        		);
//	        FisPropertyObject.getInstance().setPastParsingRule(
//	        		FisPropertyObject.getInstance().getParsingRule()
//	        		);
	        
	        return true;
        
    	} catch (Exception e) {
    		log.info("## FisCommonUtil, reloadBaseRuleData ", e);
    		return false;
    	}       
    	
    }
	
	// 준비된 기준정보를 적용 
    public boolean applicationNewBaseRulse() {
    	try {
    		
//    		FisPropertyObject.getInstance().setMappingRule(
//	        		FisPropertyObject.getInstance().getPrepMappingRule()
//	        		);
//	        FisPropertyObject.getInstance().setParsingRule(
//	        		FisPropertyObject.getInstance().getPrepParsingRule()
//	        		);
    		
    		return true;
    	} catch (Exception e) {    		
    		log.info("## FisCommonUtil, applicationNewBaseRulse ", e);
    		return false;
    	}
    	
    }
}
