package com.abs.cmn.fis.domain.rule.service;

import com.abs.cmn.fis.domain.rule.model.CnFisIfParseRule;
import com.abs.cmn.fis.domain.rule.repository.CnFisIfParsingFileInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CnFisIfParsingFileInfoService {

    private final CnFisIfParsingFileInfoRepository repository;

    public List<CnFisIfParseRule> getAllEntities(){
        return this.repository.findAll();
    }
}
