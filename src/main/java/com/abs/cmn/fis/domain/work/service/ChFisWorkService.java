package com.abs.cmn.fis.domain.work.service;

import com.abs.cmn.fis.domain.work.model.ChFisWork;
import com.abs.cmn.fis.domain.work.model.CnFisWork;
import com.abs.cmn.fis.domain.work.repository.ChFisWorkRepository;
import com.abs.cmn.fis.domain.work.repository.CnFisWorkRepository;
import com.abs.cmn.fis.domain.work.vo.ChFisWorkSaveRequestVo;
import com.abs.cmn.fis.domain.work.vo.CnFisWorkSaveRequestVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChFisWorkService {
    private final ChFisWorkRepository repository;

    public ChFisWork saveEntity(ChFisWorkSaveRequestVo vo){
        try{
            ChFisWork entity = vo.toEntity();
            log.info(entity.toString());
            return this.repository.save(entity);
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            return null;
        }

    }


}
