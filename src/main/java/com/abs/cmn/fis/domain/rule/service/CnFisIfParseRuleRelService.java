package com.abs.cmn.fis.domain.rule.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.abs.cmn.fis.domain.rule.model.CnFisIfParseRuleRel;
import com.abs.cmn.fis.domain.rule.repository.CnFisIfParseRuleRelRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CnFisIfParseRuleRelService {

    private final CnFisIfParseRuleRelRepository repository;

    public List<CnFisIfParseRuleRel> getAllEntities(){
        return this.repository.findAll();
    }
}
