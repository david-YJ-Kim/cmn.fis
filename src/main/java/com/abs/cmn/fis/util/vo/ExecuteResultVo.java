package com.abs.cmn.fis.util.vo;

import lombok.Data;

@Data
public class ExecuteResultVo {

    String workId;
    // TODO 정확한 의미 식별 필요...
    String status;

    String executeStartTime;

    String executeEndTime;


    long totalElapsedTime;

    long fileReadElapsedTime;

    long parsingElapsedTime;

    long insertElapsedTime;

    int rowCount;

    String requestFilePath;
    String targetFilePath;

    String movedFilePath;
    String movedFileName;

    MessageSendResultVo sendResultVo;







}
