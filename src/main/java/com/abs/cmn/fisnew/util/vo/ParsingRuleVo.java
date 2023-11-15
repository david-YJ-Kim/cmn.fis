package com.abs.cmn.fisnew.util.vo;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParsingRuleVo {

	private String eqpName;					// 장비명
	
	private String fileFormatType;			// 파일 정규 비정규 유형
	
	private String fileType;				// 파일 유형 검/계측
	
	private String targetFileMovePath;		// parsing 처리 후 파일 이동 장소
	
	private String parsingTitleInfo;		// 파싱 컬럼 정보 A,D,G-J,L
	
	private String[] parsingTitleInfos;		// 파싱 컬럼 A,D,G,H,I,J,L  or *

	private int[] parsingTitles;			// 컬럼 번호 0,3,6,7,8,9,11
	
	private String parsingRowInfo;			// 파싱 로우 정보 41,48-200 or *
	
	private int[] parsingRowInfos;			// 파싱 로우 41,48,49,50,...,199,200

	private int parsingColNum;				// 파싱 컬럼 번호
	
	private String fileColumName;			// 파싱 컬럼명
	
	private String[] fileColumNames;		// 파싱 컬럼 배열
	
	private String mappColumnName;			// 매핑 컬럼명
	
	private String[] mappColumns;			// 매핑 컬럼명 배열


	@Builder
	public ParsingRuleVo(String eqpName, String fileFormatType, String fileType, String targetFileMovePath, String parsingTitleInfo, String[] parsingTitleInfos, int[] parsingTitles, String parsingRowInfo, int[] parsingRowInfos, int parsingColNum, String fileColumName, String[] fileColumNames, String mappColumnName, String[] mappColumns) {
		this.eqpName = eqpName;
		this.fileFormatType = fileFormatType;
		this.fileType = fileType;
		this.targetFileMovePath = targetFileMovePath;
		this.parsingTitleInfo = parsingTitleInfo;
		this.parsingTitleInfos = parsingTitleInfos;
		this.parsingTitles = parsingTitles;
		this.parsingRowInfo = parsingRowInfo;
		this.parsingRowInfos = parsingRowInfos;
		this.parsingColNum = parsingColNum;
		this.fileColumName = fileColumName;
		this.fileColumNames = fileColumNames;
		this.mappColumnName = mappColumnName;
		this.mappColumns = mappColumns;
	}
}
