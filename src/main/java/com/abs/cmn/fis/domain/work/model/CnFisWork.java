package com.abs.cmn.fis.domain.work.model;

import com.abs.cmn.fis.util.code.FisFileType;
import com.abs.cmn.fis.util.code.ProcessStateCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "CN_FIS_WORK")
public class CnFisWork {

    @javax.persistence.Id
    @GenericGenerator(name = "CN_FIS_WORK_SEQ_GENERATOR", strategy = "com.abs.cmn.fis.util.ObjIdGenerator")
    @GeneratedValue(generator = "CN_FIS_WORK_SEQ_GENERATOR")
    @Column(name = "OBJ_ID")
    private String objId;

    @Column(name = "TRACKING_KEY")
    private String trackingKey;

    @Column(name = "FILE_NM")
    private String fileName;
    @Column(name = "FILE_PATH")
    private String filePath;

    @Enumerated(EnumType.STRING)
    @Column(name = "FILE_TYPE")
    private FisFileType fileType;
    @Column(name = "EQP_ID")
    private String eqpId;

    @Column(name = "REQ_SYS_NM")
    private String requestSystemName;

    @Enumerated(EnumType.STRING)
    @Column(name = "PROC_STATE")
    private ProcessStateCode processState;

    @Column(name = "CREATE_USER_ID")
    private String createUserId;
    @Column(name = "CREATE_DATE")
    private Timestamp createDate;

    @Column(name = "UPDATE_USER_ID")
    private String updateUserId;

    @Column(name = "UPDATE_DATE")
    private Timestamp updateDate;


    @Builder
    public CnFisWork(String objId, String trackingKey, String fileName, String filePath, FisFileType fileType, String eqpId, String requestSystemName, ProcessStateCode processState, String createUserId, Timestamp createDate, String updateUserId, Timestamp updateDate) {
        this.objId = objId;
        this.trackingKey = trackingKey;
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

    @Override
    public String toString() {
        return "CnFisWork{" +
                "objId='" + objId + '\'' +
                "trackingKey='" + trackingKey + '\'' +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileType=" + fileType +
                ", eqpId='" + eqpId + '\'' +
                ", requestSystemName='" + requestSystemName + '\'' +
                ", processState=" + processState +
                ", createUserId='" + createUserId + '\'' +
                ", createDate=" + createDate +
                ", updateUserId='" + updateUserId + '\'' +
                ", updateDate=" + updateDate +
                '}';
    }
}
