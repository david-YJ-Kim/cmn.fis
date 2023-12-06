package com.abs.cmn.fis.domain.work.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.abs.cmn.fis.domain.work.model.ChFisWork;
import com.abs.cmn.fis.domain.work.model.CnFisWork;
import com.abs.cmn.fis.domain.work.service.ChFisWorkService;
import com.abs.cmn.fis.domain.work.service.CnFisWorkService;
import com.abs.cmn.fis.domain.work.vo.ChFisWorkSaveRequestVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FisWorkTableManageController {

    @Autowired
    private static CnFisWorkService cnFisWorkService;
    
    @Autowired
    private static ChFisWorkService chFisWorkService;
    
    
    public void moveToDatasWorkHistory() {
		List<CnFisWork> rs = cnFisWorkService.getDeleteEntities();
		ChFisWorkSaveRequestVo hisData;
		for (CnFisWork vo : rs) {
			hisData = ChFisWorkSaveRequestVo.builder()
					.refObjId(vo.getObjId())
	                .fileName(vo.getFileName())
	                .fileType(vo.getFileType().name())
	                .eqpId(vo.getEqpId())
	                .requestSystemName(vo.getRequestSystemName())
	                .processState(vo.getProcessState().name())
	                .createUserId(vo.getRequestSystemName())
	                .createDate(Timestamp.valueOf(LocalDateTime.now()))
	                .updateUserId(vo.getUpdateUserId())
	                .updateDate(Timestamp.valueOf(LocalDateTime.now()))
	                .build();
			
			ChFisWork rst = chFisWorkService.saveEntity(hisData);
			log.info("## stored to history table : "+ rst.getObjId());
			cnFisWorkService.deleteEntityByObjId(vo.getObjId());
		}
	}
}
