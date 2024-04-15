package com.abs.cmn.fis.domain.rule.repository;

import java.util.List;

import com.abs.cmn.fis.domain.rule.model.CnFisParseRule;
import com.abs.cmn.fis.util.code.FisFileType;
import org.springframework.data.jpa.repository.JpaRepository;

import com.abs.cmn.fis.domain.rule.model.CnFisParseRuleRel;
import org.springframework.stereotype.Repository;

@Repository
public interface CnFisParseRuleRelRepository extends JpaRepository<CnFisParseRuleRel, Long> {

    List<CnFisParseRuleRel> findAll();

    List<CnFisParseRuleRel> findCnFisParseRuleRelsByEqpIdAndFileTyp(String eqpId, FisFileType fileTyp);

}
