package com.abs.cmn.fis.domain.work.controller;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.abs.cmn.fis.config.FisSharedInstance;
import com.abs.cmn.fis.domain.edm.repository.ParsingDataRepository;
import com.abs.cmn.fis.domain.work.vo.ChFisWorkSaveRequestVo;
import com.abs.cmn.fis.util.FisCommonUtil;
import com.abs.cmn.fis.util.code.FisConstant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class FisWorkTableManageController {

    @Autowired
    private ParsingDataRepository parsingDataRepository;

    public void startDeleteLogic() {
        moveToDatasWorkHistory();
    }

    private void moveToDatasWorkHistory() {

        String sql = FisSharedInstance.getInstance().getGetDeleteListSql();
        List<Map<String,Object>> delList = parsingDataRepository.getDeleteEntities(sql);

//		List<CnFisWork> rs = cnFisWorkService.getDeleteEntities();
        log.info("FisWorkTableManageController : moveToDatasWorkHistory() cnt : "+delList.size());
        ChFisWorkSaveRequestVo hisData;
        for (Map<String,Object> vo : delList) {
            log.info("--. objId : "+vo.toString());
            log.info("1. OBJ_ID : "+vo.get("OBJ_ID").toString());
            log.info("2. FILE_NM : "+vo.get("FILE_NM").toString());
            log.info("3. FILE_TYPE : "+vo.get("FILE_TYPE").toString());
            log.info("4. FILE_PATH : "+vo.get("FILE_PATH").toString());
            log.info("5. EQP_ID : "+vo.get("EQP_ID").toString());
            log.info("6. REQ_SYS_NM : "+vo.get("REQ_SYS_NM").toString());
            log.info("7. PROC_STATE : "+vo.get("PROC_STATE").toString());
            log.info("8. CREATE_USER_ID : "+vo.get("CREATE_USER_ID").toString());
            hisData = ChFisWorkSaveRequestVo.builder()
                    .objId(FisCommonUtil.generateObjKey())
                    .refObjId(vo.get("OBJ_ID").toString())
                    .fileName(vo.get("FILE_NM").toString())
                    .fileType(vo.get("FILE_TYPE").toString())
                    .filePath(vo.get("FILE_PATH").toString())
                    .eqpId(vo.get("EQP_ID").toString())
                    .requestSystemName(vo.get("REQ_SYS_NM").toString())
                    .processState(vo.get("PROC_STATE").toString())
                    .createUserId(vo.get("CREATE_USER_ID").toString())
                    .createDate(Timestamp.valueOf(LocalDateTime.now()))
                    .updateUserId(vo.get("UPDATE_USER_ID").toString())
                    .updateDate(Timestamp.valueOf(LocalDateTime.now()))
                    .build();

            log.info("FisWorkTableManageController : moveToDatasWorkHistory() vo : "+vo.toString());
            parsingDataRepository.insertChWork(hisData);

            log.info("before deleteBatch FILE_TYPE : "+vo.get("FILE_TYPE").toString());
            log.info("before deleteBatch OBJ_ID : "+vo.get("OBJ_ID").toString());
            log.info("before deleteBatch batchSize : "+ FisSharedInstance.getInstance().getBatchSize());

            if ( parsingDataRepository.deleteCnWork( vo.get("OBJ_ID").toString() ) )
                log.info("-- Success Delete From CnWork - ObjId : "+ vo.get("OBJ_ID").toString());
            else
                log.info("-- Failed Delete From CnWork - ObjId : "+ vo.get("OBJ_ID").toString());

            String deletBatch = FisCommonUtil.getDelteQuery( vo.get("FILE_TYPE").toString()	);
            String delResult = null;
            try {
                delResult = parsingDataRepository.deleteBatch(
                        vo.get("WORK_ID").toString(),
                        vo.get("FILE_TYPE").toString(),
                        vo.get("OBJ_ID").toString(),
                        FisSharedInstance.getInstance().getBatchSize(),
                        deletBatch );

                if ( delResult.equals(FisConstant.DELETE_BATCH.name()) )
                    log.info("(#) finished delete batch : "+vo.get("OBJ_ID").toString());
                else
                    log.info("(#) failed delete batch : "+vo.get("OBJ_ID").toString());

            } catch (SQLException e) {
                // TODO Auto-generated catch block
                log.error("@@ FisWorkTableManageController, moveToDatasWorkHistory (), e :{}", e);
            }

        }
    }
}

