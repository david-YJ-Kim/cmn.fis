package com.abs.cmn.fis.domain.rule.model;


import com.abs.cmn.fis.util.code.FisFileType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity(name = "CN_FIS_PARSE_RULE_REL")
public class CnFisParseRuleRel {

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

    @Column(name = "FILE_CLM_VAL")
    private String fileClmVal;

    @Column(name = "MPNG_CLM_NM")
    private String mpngClmNm;

    @Column(name = "CLM_DATA_TYP")
    private String clmDataTyp;

    @Column(name = "CRT_DT")
    private String crtDt;

    @Column(name = "CRT_USER_ID")
    private String crtUserId;

    @Column(name = "MDFY_DT")
    private String mdfyDt;

    @Column(name = "MDFY_USER_ID")
    private String mdfyUserId;


    @Builder
    public CnFisParseRuleRel(String objId, String eqpId, String fileTyp, String fileClmVal, String mpngClmNm,
                             String clmDataTyp, String crtDt, String crtUserId, String mdfyDt, String mdfyUserId) {
        this.objId = objId;
        this.eqpId = eqpId;
        this.fileTyp = FisFileType.valueOf(fileTyp);
        this.fileClmVal = fileClmVal;
        this.mpngClmNm = mpngClmNm;
        this.clmDataTyp = clmDataTyp;
        this.crtDt = crtDt;
        this.crtUserId = crtUserId;
        this.mdfyDt = mdfyDt;
        this.mdfyUserId = mdfyUserId;
    }


    @Override
    public String toString() {
        return "CnFisParseRuleRel{" +
                "objId='" + objId + '\'' +
                ", eqpId='" + eqpId + '\'' +
                ", fileTyp=" + fileTyp +
                ", fileClmVal='" + fileClmVal + '\'' +
                ", mpngClmNm='" + mpngClmNm + '\'' +
                ", clmDataTyp='" + clmDataTyp + '\'' +
                ", crtDt='" + crtDt + '\'' +
                ", crtUserId='" + crtUserId + '\'' +
                ", mdfyDt='" + mdfyDt + '\'' +
                ", mdfyUserId='" + mdfyUserId + '\'' +
                '}';
    }
}
