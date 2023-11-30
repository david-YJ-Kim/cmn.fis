package com.abs.cmn.fis.domain.rule.model;

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
    
    @Column(name = "EQP_NM")
    private String eqpNm;

    @Column(name = "FILE_FM_TYP")
    private char fileFmTyp;

    @Column(name = "FILE_TYP")
    private String fileTyp;

    @Column(name = "FILE_TRGT_POSN_VAL")
    private String fileTrgtPosnVal;

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
    public CnFisIfParseRule(String objId, String eqpNm, char fileFmTyp, String fileTyp, String fileTrgtPosnVal,
                                  String parsClmIdVal, String parsRowVal, Timestamp crtDt, String crtUserId,
                                  Timestamp mdfyDt, String mdfyUserId) {
        this.objId = objId;
        this.eqpNm = eqpNm;
        this.fileFmTyp = fileFmTyp;
        this.fileTyp = fileTyp;
        this.fileTrgtPosnVal = fileTrgtPosnVal;
        this.parsClmIdVal = parsClmIdVal;
        this.parsRowVal = parsRowVal;
        this.crtDt = crtDt;
        this.crtUserId = crtUserId;
        this.mdfyDt = mdfyDt;
        this.mdfyUserId = mdfyUserId;
    }

    @Override
    public String toString() {
        return "CnFisIfParsingFileInfo{" +
                "objId=" + objId +
                ", eqpNm='" + eqpNm + '\'' +
                ", fileFmTpy=" + fileFmTyp +
                ", fileTpy='" + fileTyp + '\'' +
                ", fileTrgtPosnVal='" + fileTrgtPosnVal + '\'' +
                ", parsClmIdVal='" + parsClmIdVal + '\'' +
                ", parsRowVal='" + parsRowVal + '\'' +
                ", crtDt=" + crtDt +
                ", crtUserId='" + crtUserId + '\'' +
                ", mdfyDt=" + mdfyDt +
                ", mdfyUserId='" + mdfyUserId + '\'' +
                '}';
    }
}
