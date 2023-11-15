package com.abs.cmn.fis.message.move;

public interface FisFileMoveExecute {

    void init();

    String execute(String fileType, String fileName, String filePath, String workId) throws  Exception;
}
