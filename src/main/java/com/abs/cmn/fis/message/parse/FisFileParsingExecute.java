package com.abs.cmn.fis.message.parse;

import java.util.Map;

public interface FisFileParsingExecute {

    void init();

    Map<String, String> execute(String fileType, String fileName, String filePath, String eqpId, String reqSystem) throws  Exception;
}
