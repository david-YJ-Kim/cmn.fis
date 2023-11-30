package com.abs.cmn.fis.domain.rule.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abs.cmn.fis.domain.rule.model.CnFisIfParseRule;

public interface CnFisIfParseRuleRepository extends JpaRepository<CnFisIfParseRule, Long> {
	
	@Override
	List<CnFisIfParseRule> findAll();
	
}
