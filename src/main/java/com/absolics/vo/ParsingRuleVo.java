package com.absolics.vo;


public class ParsingRuleVo {

	private String eqpName;					// 장비명
	
	private String fileFormatType;			// 파일 정규 비정규 유형
	
	private String fileType;				// 파일 유형 검/계측
	
	private String targetFileMovePath;		// parsing 처리 후 파일 이동 장소
	
	private String parsingTitleInfo;		// 파싱 컬럼 정보 A,D,G-J,L
	
	private String[] parsingTitleInfos;		// 파싱 컬럼 A,D,G,H,I,J,L
	
	private int[] parsingTitles;			// 컬럼 번호 0,3,6,7,8,9,11
	
	private String parsingRowInfo;			// 파싱 로우 정보 41,48-200
	
	private int[] parsingRowInfos;			// 파싱 로우 41,48,49,50,...,199,200
	
	private String fileColumName;			// 파싱 컬럼명
	
	private String[] fileColumNames;		// 파싱 컬럼 배열
	
	private String mappColumnName;			// 매핑 컬럼명
	
	private String[] mappColumns;			// 매핑 컬럼명 배열
	
	public ParsingRuleVo() {}
	
	public ParsingRuleVo(String fileFormat, int[] colums, int[] rows) {
		this.fileFormatType=fileFormat;
		this.parsingTitles = colums;
		this.parsingRowInfos = rows;
	}
	
	public String getEqpName() {
		return eqpName;
	}

	public void setEqpName(String eqpName) {
		this.eqpName = eqpName;
	}

	public String getFileFormatType() {
		return fileFormatType;
	}

	public void setFileFormatType(String fileFormatType) {
		this.fileFormatType = fileFormatType;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getTargetFileMovePath() {
		return targetFileMovePath;
	}

	public void setTargetFileMovePath(String targetFileMovePath) {
		this.targetFileMovePath = targetFileMovePath;
	}
	
	public String[] getParsingTitleInfos() {
		return parsingTitleInfos;
	}

	public void setParsingTitleInfo(String[] parsingTitleInfos) {
		this.parsingTitleInfos = parsingTitleInfos;
	}
	
	public int[] getParsingTitles() {
		return parsingTitles;
	}

	public void setParsingTitles(int[] parsingTitles) {
		this.parsingTitles = parsingTitles;
	}

	public String getParsingTitleInfo() {
		return parsingTitleInfo;
	}

	public void setParsingTitleInfo(String parsingTitleInfo) {
		this.parsingTitleInfo = parsingTitleInfo;
	}

	public String getParsingRowInfo() {
		return parsingRowInfo;
	}

	public void setParsingRowInfo(String parsingRowInfo) {
		this.parsingRowInfo = parsingRowInfo;
	}

	public int[] getParsingRowInfos() {
		return parsingRowInfos;
	}

	public void setParsingRowInfos(int[] parsingRowInfos) {
		this.parsingRowInfos = parsingRowInfos;
	}

	public String getFileColumName() {
		return fileColumName;
	}

	public void setFileColumName(String fileColumName) {
		this.fileColumName = fileColumName;
	}

	public String[] getFileColumNames() {
		return fileColumNames;
	}

	public void setFileColumNames(String[] fileColumNames) {
		this.fileColumNames = fileColumNames;
	}

	public String getMappColumnName() {
		return mappColumnName;
	}

	public void setMappColumnName(String mappColumnName) {
		this.mappColumnName = mappColumnName;
	}

	public String[] getMappColumns() {
		return mappColumns;
	}

	public void setMappColumns(String[] mappColumns) {
		this.mappColumns = mappColumns;
	}

}
