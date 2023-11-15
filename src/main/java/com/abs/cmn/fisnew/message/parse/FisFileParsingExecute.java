package com.abs.cmn.fisnew.message.parse;

public interface FisFileParsingExecute {

    void init();

    String execute(String fileType, String fileName, String filePath, String eqpId, String reqSystem, String fileFormatType) throws  Exception;
}
