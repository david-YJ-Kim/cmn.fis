package com.abs.cmn.fis.domain.work.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.abs.cmn.fis.domain.work.model.CnFisWork;
import com.abs.cmn.fis.domain.work.repository.CnFisWorkRepository;
import com.abs.cmn.fis.domain.work.vo.CnFisWorkSaveRequestVo;
import com.abs.cmn.fis.util.code.ProcessStateCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CnFisWorkService {
    private final CnFisWorkRepository cnFisWorkRepository;

    public CnFisWork saveEntity(CnFisWorkSaveRequestVo vo){
        try{
            CnFisWork entity = vo.toEntity();
            log.info(entity.toString());
            return this.cnFisWorkRepository.save(entity);
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            return null;
        }

    }

    // return work_id
    public Optional<CnFisWork> getEntityByObjId(String objId){
        return Optional.of(this.cnFisWorkRepository.findById(objId).get());
    }

    // update work_id status
    public void updateEntity(String objId, ProcessStateCode value) {
        Optional<CnFisWork> vo = this.cnFisWorkRepository.findById(objId);

        if ( vo.isPresent() ) {
            CnFisWork row = vo.get();
            row.setProcessState(value);
            this.cnFisWorkRepository.save(row);
        } else {
            log.error("[updateEntity] Occured Error : "+objId+" value : "+value.name());
        }
    }

    // delete work_id
    public void deleteEntityByObjId(String objId){
        this.cnFisWorkRepository.deleteById(objId);
    }

//    public List<CnFisWork> getDeleteEntities() {
//
//    	log.info("1. CnFisWorkService , getDeleteEntities() ");
//    	String[] values = {ProcessStateCode.C.name(), ProcessStateCode.D.name()};
//    	Iterable<String> statuses = Arrays.asList(values);
////    	String sql = "SELECT * FROM CN_FIS_WORK cfw WHERE PROC_STATE ='C'OR PROC_STATE ='D'";
//    	List<CnFisWork> delList = this.cnFisWorkRepository.findAllById(statuses);
//
//    	log.info("3. CnFisWorkService , getDeleteEntities() ");
//
//    	return delList;
//    }
}
