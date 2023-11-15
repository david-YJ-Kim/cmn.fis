package com.abs.cmn.fisnew.domain.rule.service;

import com.abs.cmn.fisnew.domain.rule.model.CnFisIfParsingDataMappingInfo;
import com.abs.cmn.fisnew.domain.rule.repository.CnFisIfParsingDataMappingInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CnFisIfParsingDataMappingInfoService {

    private final CnFisIfParsingDataMappingInfoRepository repository;

    public List<CnFisIfParsingDataMappingInfo> getAllEntities(){
        return this.repository.findAll();
    }
}
