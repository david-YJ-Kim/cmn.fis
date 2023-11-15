package com.abs.cmn.fisnew.domain.edm.model;

import com.abs.cmn.fisnew.util.FisCommonUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@Getter
public class CnFisEdcTmpVo {

    private String objId;
    private String workId;
    private String fileTp;
    private String fileFmTp;
    private String siteId;
    private String prodDefId;
    private String procDefId;
    private String procSgmtId;
    private String eqpId;
    private String lotId;
    private String prodMtrlId;
    private String subProdMtrlId;
    private String mtrlFaceCd;
    private String inspReptCnt;
    private String xVal;
    private String yVal;
    private String zVal;
    private String dcitemId;
    private String rsltVal;
    private String grdId;
    private String dfctId;
    private String dfctXVal;
    private String dfctYVal;
    private String inspDt;
    private String imgFileNm;
    private String reviewImgFileNm;
    private String inspFileNm;
    private String attr1;
    private String attr2;
    private String attrN;
    private Timestamp crtDt;
    private String fileNm;

    @Builder
    public CnFisEdcTmpVo(String workId, String fileTp, String fileFmTp, String siteId, String prodDefId, String procDefId, String procSgmtId, String eqpId, String lotId, String prodMtrlId, String subProdMtrlId, String mtrlFaceCd, String inspReptCnt, String xVal, String yVal, String zVal, String dcitemId, String rsltVal, String grdId, String dfctId, String dfctXVal, String dfctYVal, String inspDt, String imgFileNm, String reviewImgFileNm, String inspFileNm, String attr1, String attr2, String attrN, Timestamp crtDt, String fileNm) {
        this.objId = FisCommonUtil.generateObjKey();
        this.workId = workId;
        this.fileTp = fileTp;
        this.fileFmTp = fileFmTp;
        this.siteId = siteId;
        this.prodDefId = prodDefId;
        this.procDefId = procDefId;
        this.procSgmtId = procSgmtId;
        this.eqpId = eqpId;
        this.lotId = lotId;
        this.prodMtrlId = prodMtrlId;
        this.subProdMtrlId = subProdMtrlId;
        this.mtrlFaceCd = mtrlFaceCd;
        this.inspReptCnt = inspReptCnt;
        this.xVal = xVal;
        this.yVal = yVal;
        this.zVal = zVal;
        this.dcitemId = dcitemId;
        this.rsltVal = rsltVal;
        this.grdId = grdId;
        this.dfctId = dfctId;
        this.dfctXVal = dfctXVal;
        this.dfctYVal = dfctYVal;
        this.inspDt = inspDt;
        this.imgFileNm = imgFileNm;
        this.reviewImgFileNm = reviewImgFileNm;
        this.inspFileNm = inspFileNm;
        this.attr1 = attr1;
        this.attr2 = attr2;
        this.attrN = attrN;
        this.crtDt = crtDt;
        this.fileNm = fileNm;
    }


    @Override
    public String toString() {
        return "CnFisEdcTmpVo{" +
                "objId='" + objId + '\'' +
                ", workId='" + workId + '\'' +
                ", fileTp='" + fileTp + '\'' +
                ", fileFmTp='" + fileFmTp + '\'' +
                ", siteId='" + siteId + '\'' +
                ", prodDefId='" + prodDefId + '\'' +
                ", procDefId='" + procDefId + '\'' +
                ", procSgmtId='" + procSgmtId + '\'' +
                ", eqpId='" + eqpId + '\'' +
                ", lotId='" + lotId + '\'' +
                ", prodMtrlId='" + prodMtrlId + '\'' +
                ", subProdMtrlId='" + subProdMtrlId + '\'' +
                ", mtrlFaceCd='" + mtrlFaceCd + '\'' +
                ", inspReptCnt='" + inspReptCnt + '\'' +
                ", xVal='" + xVal + '\'' +
                ", yVal='" + yVal + '\'' +
                ", zVal='" + zVal + '\'' +
                ", dcitemId='" + dcitemId + '\'' +
                ", rsltVal='" + rsltVal + '\'' +
                ", grdId='" + grdId + '\'' +
                ", dfctId='" + dfctId + '\'' +
                ", dfctXVal='" + dfctXVal + '\'' +
                ", dfctYVal='" + dfctYVal + '\'' +
                ", inspDt='" + inspDt + '\'' +
                ", imgFileNm='" + imgFileNm + '\'' +
                ", reviewImgFileNm='" + reviewImgFileNm + '\'' +
                ", inspFileNm='" + inspFileNm + '\'' +
                ", attr1='" + attr1 + '\'' +
                ", attr2='" + attr2 + '\'' +
                ", attrN='" + attrN + '\'' +
                ", crtDt=" + crtDt +
                ", fileNm='" + fileNm + '\'' +
                '}';
    }
}
