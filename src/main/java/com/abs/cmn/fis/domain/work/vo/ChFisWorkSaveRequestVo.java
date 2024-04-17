package com.abs.cmn.fis.domain.work.vo;

import java.sql.Timestamp;

import com.abs.cmn.fis.domain.work.model.ChFisWork;
import com.abs.cmn.fis.domain.work.model.CnFisWork;
import com.abs.cmn.fis.util.code.FisFileType;
import com.abs.cmn.fis.util.code.ProcessStateCode;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ChFisWorkSaveRequestVo {

    private String refObjId;
    private String fileName;
    private String filePath;
    private String fileType;
    private String eqpId;
    private String requestSystemName;
    private String processState;
    private String createUserId;
    private Timestamp createDate;
    private String updateUserId;
    private Timestamp updateDate;

    @Builder
    public ChFisWorkSaveRequestVo(String objId, String refObjId, String fileName, String filePath, String fileType, String eqpId, String requestSystemName, String processState, String createUserId, Timestamp createDate, String updateUserId, Timestamp updateDate) {
        this.refObjId = refObjId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileType = fileType;
        this.eqpId = eqpId;
        this.requestSystemName = requestSystemName;
        this.processState = processState;
        this.createUserId = createUserId;
        this.createDate = createDate;
        this.updateUserId = updateUserId;
        this.updateDate = updateDate;
    }

    public ChFisWorkSaveRequestVo(CnFisWork cnFisWork) {
        this.refObjId = cnFisWork.getObjId();
        this.fileName = cnFisWork.getFileName();
        this.filePath = cnFisWork.getFilePath();
        this.fileType = String.valueOf(cnFisWork.getFileType());
        this.eqpId = cnFisWork.getEqpId();
        this.requestSystemName = cnFisWork.getRequestSystemName();
        this.processState = String.valueOf(cnFisWork.getProcessState());
        this.createUserId = cnFisWork.getCreateUserId();
        this.createDate = cnFisWork.getCreateDate();
        this.updateUserId = cnFisWork.getUpdateUserId();
        this.updateDate = cnFisWork.getUpdateDate();
    }

    public ChFisWork toEntity(){
        return ChFisWork.builder()
                .refObjId(refObjId)
                .fileName(fileName)
                .filePath(filePath)
                .fileType(FisFileType.valueOf(fileType))
                .eqpId(eqpId)
                .requestSystemName(requestSystemName)
                .processState(ProcessStateCode.valueOf(processState))
                .createUserId(createUserId)
                .createDate(createDate)
                .updateUserId(updateUserId)
                .updateDate(updateDate)
                .build();
    }

    @Override
    public String toString() {
        return "ChFisWorkSaveRequestVo{" +
                ", refObjId='" + refObjId + '\'' +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileType='" + fileType + '\'' +
                ", eqpId='" + eqpId + '\'' +
                ", requestSystemName='" + requestSystemName + '\'' +
                ", processState='" + processState + '\'' +
                ", createUserId='" + createUserId + '\'' +
                ", createDate=" + createDate +
                ", updateUserId='" + updateUserId + '\'' +
                ", updateDate=" + updateDate +
                '}';
    }
}
