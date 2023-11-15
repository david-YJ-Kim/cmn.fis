package com.abs.cmn.fisnew.domain.rule.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Entity(name = "CN_FIS_IF_PARSING_FILE_INFO")
public class CnFisIfParsingFileInfo {

    @javax.persistence.Id
    @GeneratedValue(generator = "psSeqGenerator")
    @GenericGenerator(name = "psSeqGenerator", strategy = "increment")
    @Column(name = "PS_SEQ", nullable = false)
    private Long psSeq;

    @Column(name = "EQP_NM")
    private String eqpNm;

    @Column(name = "FILE_FM_TP")
    private char fileFmTp;

    @Column(name = "FILE_TP")
    private String fileTp;

    @Column(name = "TG_FILE_MV_PT")
    private String tgFileMvPt;

    @Column(name = "PRS_TTL_INFO")
    private String prsTtlInfo;

    @Column(name = "PRS_ROW_INFO")
    private String prsRowInfo;

    @Column(name = "CRT_DT", nullable = false)
    private Timestamp crtDt;

    @Column(name = "CRTR")
    private String crtr;

    @Column(name = "MDFT_DT")
    private Timestamp mdftDt;

    @Column(name = "MDFTR")
    private String mdftr;

    @Builder
    public CnFisIfParsingFileInfo(Long psSeq, String eqpNm, char fileFmTp, String fileTp, String tgFileMvPt,
                                  String prsTtlInfo, String prsRowInfo, Timestamp crtDt, String crtr,
                                  Timestamp mdftDt, String mdftr) {
        this.psSeq = psSeq;
        this.eqpNm = eqpNm;
        this.fileFmTp = fileFmTp;
        this.fileTp = fileTp;
        this.tgFileMvPt = tgFileMvPt;
        this.prsTtlInfo = prsTtlInfo;
        this.prsRowInfo = prsRowInfo;
        this.crtDt = crtDt;
        this.crtr = crtr;
        this.mdftDt = mdftDt;
        this.mdftr = mdftr;
    }

    @Override
    public String toString() {
        return "CnFisIfParsingFileInfo{" +
                "psSeq=" + psSeq +
                ", eqpNm='" + eqpNm + '\'' +
                ", fileFmTp=" + fileFmTp +
                ", fileTp='" + fileTp + '\'' +
                ", tgFileMvPt='" + tgFileMvPt + '\'' +
                ", prsTtlInfo='" + prsTtlInfo + '\'' +
                ", prsRowInfo='" + prsRowInfo + '\'' +
                ", crtDt=" + crtDt +
                ", crtr='" + crtr + '\'' +
                ", mdftDt=" + mdftDt +
                ", mdftr='" + mdftr + '\'' +
                '}';
    }
}
