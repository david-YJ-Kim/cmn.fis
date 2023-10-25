package com.absolics.dao;


public class ParsingRuleDao {

	private String eqpName;
	
	private char fileFormatType;
	
	private String fileType;
	
	private String targetFileMovePath;
	
	private String[] parsingTitleInfo;
	
	private String[] parsingRowInfo;
	
	private String parsingTitleName;
	
	private String fileColumName;
	
	private String mappColumnName;
	
	private String psRowinfo;
	
	private int[] rowNums;
	
	public String getEqpName() {
		return eqpName;
	}

	public void setEqpName(String eqpName) {
		this.eqpName = eqpName;
	}

	public char getFileFormatType() {
		return fileFormatType;
	}

	public void setFileFormatType(char fileFormatType) {
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
	
	public String[] getParsingTitleInfo() {
		return parsingTitleInfo;
	}

	public void setParsingTitleInfo(String[] parsingTitleInfo) {
		this.parsingTitleInfo = parsingTitleInfo;
	}
	
	public String[] getParsingRowInfo() {
		return parsingRowInfo;
	}

	public void setParsingRowInfo(String[] parsingRowInfo) {
		this.parsingRowInfo = parsingRowInfo;
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
