package com.abs.cmn.fis.domain.rule.service;

import com.abs.cmn.fis.domain.rule.model.CnFisIfParseRuleRel;
import com.abs.cmn.fis.domain.rule.repository.CnFisIfParseRuleRelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CnFisIfParseRuleRelService {

    private final CnFisIfParseRuleRelRepository repository;

    public List<CnFisIfParseRuleRel> getAllEntities(){
        return this.repository.findAll();
    }
}