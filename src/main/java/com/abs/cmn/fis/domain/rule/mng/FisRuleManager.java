package com.abs.cmn.fis.domain.rule.mng;

public interface FisRuleManager {

    public void init();

    public boolean reloadBaseRuleData() ;
    public boolean applicationNewBaseRulse();

    public void initializeRuleData();
}
