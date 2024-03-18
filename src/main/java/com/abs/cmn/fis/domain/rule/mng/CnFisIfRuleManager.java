package com.abs.cmn.fis.domain.rule.mng;

import java.util.List;

import com.abs.cmn.fis.domain.rule.model.CnFisIfParseRule;
import com.abs.cmn.fis.domain.rule.model.CnFisIfParseRuleRel;
import com.abs.cmn.fis.util.vo.ParseRuleVo;

public interface CnFisIfRuleManager {

    public void init();

    public boolean reloadBaseRuleData() ;
    public boolean applicationNewBaseRulse();

    // 김연준 매니저 작성
    public void initializeRuleData();
    public ParseRuleVo setParsingRuleVo(CnFisIfParseRule ruleEntity, List<CnFisIfParseRuleRel> relations);

}
