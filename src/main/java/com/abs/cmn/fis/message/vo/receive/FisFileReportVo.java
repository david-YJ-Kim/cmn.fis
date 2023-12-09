package com.abs.cmn.fis.message.vo.receive;

import com.abs.cmn.fis.message.vo.FisMsgCommonVo;
import com.abs.cmn.fis.message.vo.common.FisMsgBody;
import com.abs.cmn.fis.util.code.FisFileType;

import lombok.Data;



@Data
public class FisFileReportVo extends FisMsgCommonVo {

    FisFileReportBody body;

    @Data
    public static class FisFileReportBody extends FisMsgBody {

        String eqpId;
        FisFileType fileType;
        String filePath;
        String fileName;

    }
}
