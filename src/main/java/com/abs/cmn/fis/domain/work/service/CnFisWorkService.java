package com.abs.cmn.fis.domain.work.service;

import com.abs.cmn.fis.domain.work.model.CnFisWork;
import com.abs.cmn.fis.domain.work.repository.CnFisWorkRepository;
import com.abs.cmn.fis.domain.work.vo.CnFisWorkSaveRequestVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CnFisWorkService {
    private final CnFisWorkRepository repository;

    public CnFisWork saveEntity(CnFisWorkSaveRequestVo vo){
        try{
            CnFisWork entity = vo.toEntity();
            log.info(entity.toString());
            return this.repository.save(entity);
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            return null;
        }

    }

    // return work_id
    public Optional<CnFisWork> getEntityByObjId(String objId){
        return Optional.of(this.repository.findById(objId).get());
    }
    
    // update work_id status
    public void updateEntity(String objId, String status) {
    	Optional<CnFisWork> vo = this.repository.findById(objId);
    	
    }

    // delete work_id
    public void deleteEntityByObjId(String objId){
        this.repository.deleteById(objId);
    }

}
