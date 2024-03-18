package com.abs.cmn.fis.domain.rule.model;

import com.abs.cmn.fis.util.code.FisFileType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Entity(name = "CN_FIS_IF_PARSE_RULE")
public class CnFisIfParseRule {

    @javax.persistence.Id
    @GenericGenerator(name = "CN_FIS_WORK_SEQ_GENERATOR", strategy = "com.abs.cmn.fis.util.ObjIdGenerator")
    @GeneratedValue(generator = "CN_FIS_WORK_SEQ_GENERATOR")
    @Column(name = "OBJ_ID", nullable = false)
    private String objId;

    @Column(name = "EQP_ID")
    private String eqpId;

    @Column(name = "FILE_TYP")
    @Enumerated(EnumType.STRING)
    private FisFileType fileTyp;

    @Column(name = "START_HDR_VAL")
    private int startHdrVal;

    @Column(name = "FILE_TGT_POSN_VAL")
    private String fileTgtPosnVal;

    @Column(name = "PARS_CLM_ID_VAL")
    private String parsClmIdVal;

    @Column(name = "PARS_ROW_VAL")
    private String parsRowVal;

    @Column(name = "CRT_DT", nullable = false)
    private Timestamp crtDt;

    @Column(name = "CRT_USER_ID")
    private String crtUserId;

    @Column(name = "MDFY_DT")
    private Timestamp mdfyDt;

    @Column(name = "MDFY_USER_ID")
    private String mdfyUserId;

    @Builder
    public CnFisIfParseRule(String objId, String eqpId,String fileTyp, int startHdrVal, String fileTgtPosnVal,
                            String parsClmIdVal, String parsRowVal, Timestamp crtDt, String crtUserId,
                            Timestamp mdfyDt, String mdfyUserId) {
        this.objId = objId;
        this.eqpId = eqpId;
        this.fileTyp = FisFileType.valueOf(fileTyp);
        this.startHdrVal = startHdrVal;
        this.fileTgtPosnVal = fileTgtPosnVal;
        this.parsClmIdVal = parsClmIdVal;
        this.parsRowVal = parsRowVal;
        this.crtDt = crtDt;
        this.crtUserId = crtUserId;
        this.mdfyDt = mdfyDt;
        this.mdfyUserId = mdfyUserId;
    }

    @Override
    public String toString() {
        return "CnFisIfParseRule{" +
                "objId='" + objId + '\'' +
                ", eqpId='" + eqpId + '\'' +
                ", fileTyp='" + fileTyp + '\'' +
                ", startHdrVal=" + startHdrVal +
                ", fileTgtPosnVal='" + fileTgtPosnVal + '\'' +
                ", parsClmIdVal='" + parsClmIdVal + '\'' +
                ", parsRowVal='" + parsRowVal + '\'' +
                ", crtDt=" + crtDt +
                ", crtUserId='" + crtUserId + '\'' +
                ", mdfyDt=" + mdfyDt +
                ", mdfyUserId='" + mdfyUserId + '\'' +
                '}';
    }
}
