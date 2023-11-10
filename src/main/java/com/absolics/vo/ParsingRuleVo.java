package com.absolics.vo;


public class ParsingRuleVo {

	private String eqpName;
	
	private String fileFormatType;
	
	private String fileType;
	
	private String targetFileMovePath;
	
	private String parsingTitleInfo; //1,2,3,7-12,16,20
	
	private String[] parsingTitleInfos; // {1,2,3,7,8,9,10,11,12,16,20}
	
	private String parsingRowInfo; //1,2,3,7-12,16,20
	
	private int[] parsingRowInfos;  // {1,2,3,7,8,9,10,11,12,16,20}
	
	private String parsingTitleName;
	
	private String fileColumName;
	
	private String mappColumnName;
	
	private String psRowinfo;
	
	private int[] rowNums;
	
	public ParsingRuleVo() {}
	
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
	
	public String getParsingTitleName() {
		return parsingTitleName;
	}

	public void setParsingTitleName(String parsingTitleName) {
		this.parsingTitleName = parsingTitleName;
	}

	public String getFileColumName() {
		return fileColumName;
	}

	public void setFileColumName(String fileColumName) {
		this.fileColumName = fileColumName;
	}

	public String getMappColumnName() {
		return mappColumnName;
	}

	public void setMappColumnName(String mappColumnName) {
		this.mappColumnName = mappColumnName;
	}

	public int[] getRowNums() {
		return rowNums;
	}

	public void setRowNums(int[] rowNums) {
		this.rowNums = rowNums;
	}

	public String getPsRowinfo() {
		return psRowinfo;
	}

	public void setPsRowinfo(String psRowinfo) {
		this.psRowinfo = psRowinfo;
	}

}
