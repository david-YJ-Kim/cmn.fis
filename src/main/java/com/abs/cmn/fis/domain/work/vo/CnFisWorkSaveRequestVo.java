package com.abs.cmn.fis.domain.work.vo;

import com.abs.cmn.fis.domain.work.model.CnFisWork;
import com.abs.cmn.fis.util.code.FisFileType;
import com.abs.cmn.fis.util.code.ProcessStateCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
public class CnFisWorkSaveRequestVo {

    private String workId;
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
    public CnFisWorkSaveRequestVo(String workId, String fileName, String filePath, String fileType, String eqpId, String requestSystemName, String processState, String createUserId, Timestamp createDate, String updateUserId, Timestamp updateDate) {
        this.workId = workId;
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

    public CnFisWork toEntity(){
        return CnFisWork.builder()
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
        return "CnFisWorkSaveRequestVo{" +
                "fileName='" + fileName + '\'' +
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
