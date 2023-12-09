package com.abs.cmn.fis.message.parse;

import com.abs.cmn.fis.message.vo.receive.FisFileReportVo;
import com.abs.cmn.fis.util.vo.ExecuteResultVo;

public interface FisFileParsingExecute{

    void init();

    ExecuteResultVo execute(FisFileReportVo fisFileReportVo, String ackKey) throws  Exception;
}
