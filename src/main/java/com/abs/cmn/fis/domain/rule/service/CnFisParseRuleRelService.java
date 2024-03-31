package com.abs.cmn.fis.domain.rule.service;

import java.util.List;

import com.abs.cmn.fis.domain.rule.model.CnFisParseRule;
import com.abs.cmn.fis.util.code.FisFileType;
import org.springframework.stereotype.Service;

import com.abs.cmn.fis.domain.rule.model.CnFisParseRuleRel;
import com.abs.cmn.fis.domain.rule.repository.CnFisParseRuleRelRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CnFisParseRuleRelService {

    private final CnFisParseRuleRelRepository repository;

    public CnFisParseRuleRel save(CnFisParseRuleRel entity){
        return this.repository.save(entity);
    }

    public List<CnFisParseRuleRel> getAllEntities(){
        return this.repository.findAll();
    }

    public List<CnFisParseRuleRel> findCnFisParseRuleRelsByEqpIdAndFileTyp(String eqpId, FisFileType fileTyp){
        return this.repository.findCnFisParseRuleRelsByEqpIdAndFileTyp(eqpId, fileTyp);
    }

}
