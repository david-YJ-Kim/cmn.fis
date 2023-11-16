package com.abs.cmn.fis.domain.rule.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abs.cmn.fis.domain.rule.model.CnFisIfParsingFileInfo;

public interface CnFisIfParsingFileInfoRepository extends JpaRepository<CnFisIfParsingFileInfo, Long> {
	
	@Override
	List<CnFisIfParsingFileInfo> findAll();
	
}
