package com.abs.cmn.fis.domain.rule.model;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;

@NoArgsConstructor
@Getter
@Entity(name = "CN_FIS_IF_PARSING_DATA_MAPPING_INFO")
public class CnFisIfParsingDataMappingInfo {

    @javax.persistence.Id
    @GenericGenerator(name = "pmSeqGenerator", strategy = "increment")
    @Column(name = "PM_SEQ", nullable = false)
    private Long pmSeq;

    @Column(name = "EQP_NM")
    private String eqpNm;

    @Column(name = "FILE_FM_TP")
    private String fileFmTp;

    @Column(name = "FILE_TP")
    private String fileTp;

    @Column(name = "FILE_COLM_NUM")
    private Integer fileColmNum;

    @Column(name = "FILE_CLM_NM")
    private String fileClmNm;

    @Column(name = "MPP_CLM_NM")
    private String mppClmNm;

    @Column(name = "CRT_DT")
    private String crtDt;

    @Column(name = "CRTR")
    private String crtr;

    @Column(name = "MDFT_DT")
    private String mdftDt;

    @Column(name = "MDFTR")
    private String mdftr;


    @Builder
    public CnFisIfParsingDataMappingInfo(Long pmSeq, String eqpNm, String fileFmTp, String fileTp, Integer fileColmNum, String fileClmNm, String mppClmNm, String crtDt, String crtr, String mdftDt, String mdftr) {
        this.pmSeq = pmSeq;
        this.eqpNm = eqpNm;
        this.fileFmTp = fileFmTp;
        this.fileTp = fileTp;
        this.fileColmNum = fileColmNum;
        this.fileClmNm = fileClmNm;
        this.mppClmNm = mppClmNm;
        this.crtDt = crtDt;
        this.crtr = crtr;
        this.mdftDt = mdftDt;
        this.mdftr = mdftr;
    }

    @Override
    public String toString() {
        return "CnFisIfParsingDataMappingInfo{" +
                "pmSeq=" + pmSeq +
                ", eqpNm='" + eqpNm + '\'' +
                ", fileFmTp='" + fileFmTp + '\'' +
                ", fileTp='" + fileTp + '\'' +
                ", fileColmNum='" + fileColmNum + '\'' +
                ", fileClmNm='" + fileClmNm + '\'' +
                ", mppClmNm='" + mppClmNm + '\'' +
                ", crtDt='" + crtDt + '\'' +
                ", crtr='" + crtr + '\'' +
                ", mdftDt='" + mdftDt + '\'' +
                ", mdftr='" + mdftr + '\'' +
                '}';
    }
}
