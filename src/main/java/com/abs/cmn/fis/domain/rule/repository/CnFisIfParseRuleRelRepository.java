package com.abs.cmn.fis.domain.rule.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abs.cmn.fis.domain.rule.model.CnFisIfParseRuleRel;

public interface CnFisIfParseRuleRelRepository extends JpaRepository<CnFisIfParseRuleRel, Long> {

    List<CnFisIfParseRuleRel> findAll();

}
