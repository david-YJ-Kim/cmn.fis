package com.abs.cmn.fisnew.message.move;

public interface FisFileMoveExecute {

    void init();

    String execute(String fileType, String fileName, String filePath, String workId) throws  Exception;
}
