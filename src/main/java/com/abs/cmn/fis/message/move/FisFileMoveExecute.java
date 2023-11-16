package com.abs.cmn.fis.message.move;

public interface FisFileMoveExecute {

    void init();

    void execute(String fileType, String fileName, String filePath, String workId) throws  Exception;
}
