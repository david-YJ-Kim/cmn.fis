package com.abs.cmn.fis.domain.rule.model;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;

@NoArgsConstructor
@Getter
@Entity(name = "CN_FIS_IF_PARSE_RULE_REL")
public class CnFisIfParseRuleRel {

    @javax.persistence.Id
    @GenericGenerator(name = "pmSeqGenerator", strategy = "increment")
    @Column(name = "OBJ_ID", nullable = false)
    private String objId;
    
    @Column(name = "REF_OBJ_ID", nullable = false)
    private String refObjId;

    @Column(name = "FILE_CLM_VAL")
    private String fileClmVal;

    @Column(name = "FILE_CLM_NM")
    private String fileClmNm;

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
    public CnFisIfParseRuleRel(String objId, String refObjId, String fileClmVal, String fileClmNm, String mpngClmNm, 
    							String clmDataTyp, String crtDt, String crtUserId, String mdfyDt, String mdfyUserId) {
        this.objId = objId;
        this.refObjId = refObjId;
        this.fileClmVal = fileClmVal;
        this.fileClmNm = fileClmNm;
        this.mpngClmNm = mpngClmNm;
        this.clmDataTyp = clmDataTyp;
        this.crtDt = crtDt;
        this.crtUserId = crtUserId;
        this.mdfyDt = mdfyDt;
        this.mdfyUserId = mdfyUserId;
    }

    @Override
    public String toString() {
        return "CnFisIfParsingDataMappingInfo{" +
                "objId=" + objId +
                ", refObjId='" + refObjId + '\'' +
                ", fileClmVal='" + fileClmVal + '\'' +
                ", fileClmNm='" + fileClmNm + '\'' +
                ", mpngClmNm='" + mpngClmNm + '\'' +
                ", clmDataTyp='" + clmDataTyp + '\'' +
                ", crtDt='" + crtDt + '\'' +
                ", crtUserId='" + crtUserId + '\'' +
                ", mdfyDt='" + mdfyDt + '\'' +
                ", mdfyUserId='" + mdfyUserId + '\'' +
                '}';
    }
}
