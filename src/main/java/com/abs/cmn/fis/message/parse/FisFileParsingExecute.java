package com.abs.cmn.fis.message.parse;

public interface FisFileParsingExecute {

    void init();

    String execute(String fileType, String fileName, String filePath, String eqpId, String reqSystem, String fileFormatType) throws  Exception;
}
