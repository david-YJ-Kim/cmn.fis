package com.abs.cmn.fis.domain.rule.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abs.cmn.fis.domain.rule.model.CnFisParseRule;

public interface CnFisParseRuleRepository extends JpaRepository<CnFisParseRule, Long> {

    @Override
    List<CnFisParseRule> findAll();

}
