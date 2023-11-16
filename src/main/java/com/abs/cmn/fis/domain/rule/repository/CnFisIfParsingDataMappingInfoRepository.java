package com.abs.cmn.fis.domain.rule.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abs.cmn.fis.domain.rule.model.CnFisIfParsingDataMappingInfo;

public interface CnFisIfParsingDataMappingInfoRepository extends JpaRepository<CnFisIfParsingDataMappingInfo, Long> {
		
	List<CnFisIfParsingDataMappingInfo> findAll();
	
}
