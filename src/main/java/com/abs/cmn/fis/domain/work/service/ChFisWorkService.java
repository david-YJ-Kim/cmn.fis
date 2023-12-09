package com.abs.cmn.fis.domain.work.service;

import org.springframework.stereotype.Service;

import com.abs.cmn.fis.domain.work.model.ChFisWork;
import com.abs.cmn.fis.domain.work.repository.ChFisWorkRepository;
import com.abs.cmn.fis.domain.work.vo.ChFisWorkSaveRequestVo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChFisWorkService {
    private final ChFisWorkRepository chWorkRepository;

    public ChFisWork saveEntity(ChFisWorkSaveRequestVo vo){
        try{
            ChFisWork entity = vo.toEntity();
            log.info(entity.toString());
            return this.chWorkRepository.save(entity);
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            return null;
        }

    }
    
}
