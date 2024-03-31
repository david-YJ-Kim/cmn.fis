package com.abs.cmn.fis.domain.rule.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.abs.cmn.fis.domain.rule.model.CnFisParseRule;
import com.abs.cmn.fis.domain.rule.repository.CnFisParseRuleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CnFisParseRuleService {

    private final CnFisParseRuleRepository repository;

    public List<CnFisParseRule> getAllEntities(){
        return this.repository.findAll();
    }
}
