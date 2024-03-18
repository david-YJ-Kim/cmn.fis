package com.abs.cmn.fis.domain.rule.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.abs.cmn.fis.domain.rule.model.CnFisIfParseRule;
import com.abs.cmn.fis.domain.rule.repository.CnFisIfParseRuleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CnFisIfParseRuleService {

    private final CnFisIfParseRuleRepository repository;

    public List<CnFisIfParseRule> getAllEntities(){
        return this.repository.findAll();
    }
}
