package com.abs.cmn.fisnew.util.vo;

import lombok.Builder;
import lombok.Data;

@Data
public class ParsingDataVo {
	/**
	 * 파싱 데이터 입력 객체
	 **/
	
	private String workId;
	private String fileFmYn;
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
	private String xValue;
	private String yValue;
	private String zValue;
	private String dcitemId;
	private String rsltVal;
	private String grdId;
	private String dfctId;
	private String dfctXvalue;
	private String dfctYvalue;
	private String inspDt;
	private String imgFileName;
	private String reviewImgFileName;
	private String inspFileName;
	private String attr1;
	private String attr2;
	private String attrN;
	private String prcsState;

	@Builder
	public ParsingDataVo(String workId, String fileFmYn, String siteId, String prodDefId, String procDefId, String procSgmtId, String eqpId, String lotId, String prodMtrlId, String subProdMtrlId, String mtrlFaceCd, String inspReptCnt, String xValue, String yValue, String zValue, String dcitemId, String rsltVal, String grdId, String dfctId, String dfctXvalue, String dfctYvalue, String inspDt, String imgFileName, String reviewImgFileName, String inspFileName, String attr1, String attr2, String attrN, String prcsState) {
		this.workId = workId;
		this.fileFmYn = fileFmYn;
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
		this.xValue = xValue;
		this.yValue = yValue;
		this.zValue = zValue;
		this.dcitemId = dcitemId;
		this.rsltVal = rsltVal;
		this.grdId = grdId;
		this.dfctId = dfctId;
		this.dfctXvalue = dfctXvalue;
		this.dfctYvalue = dfctYvalue;
		this.inspDt = inspDt;
		this.imgFileName = imgFileName;
		this.reviewImgFileName = reviewImgFileName;
		this.inspFileName = inspFileName;
		this.attr1 = attr1;
		this.attr2 = attr2;
		this.attrN = attrN;
		this.prcsState = prcsState;
	}
}
