package com.absolics.dao;


public class ParsingRuleDao {

	private String eqpName;
	
	private char fileFormatType;
	
	private String fileType;
	
	private String filepath;
	
	private String fileCulomnInfo;
	
	private int psStartRow;
	
	private int psEndRow;
	
	private String additionRow;

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

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getFileCulomnInfo() {
		return fileCulomnInfo;
	}

	public void setFileCulomnInfo(String fileCulomnInfo) {
		this.fileCulomnInfo = fileCulomnInfo;
	}

	public int getPsStartRow() {
		return psStartRow;
	}

	public void setPsStartRow(int psStartRow) {
		this.psStartRow = psStartRow;
	}

	public int getPsEndRow() {
		return psEndRow;
	}

	public void setPsEndRow(int psEndRow) {
		this.psEndRow = psEndRow;
	}

	public String getAdditionRow() {
		return additionRow;
	}

	public void setAdditionRow(String additionRow) {
		this.additionRow = additionRow;
	}
	
}
