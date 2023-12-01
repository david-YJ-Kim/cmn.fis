package com.abs.cmn.fis.message.vo.receive;

import com.abs.cmn.fis.message.vo.FisMsgCommonVo;
import com.abs.cmn.fis.message.vo.common.FisMsgBody;
import lombok.Data;



@Data
public class FisFileReportVo extends FisMsgCommonVo {

    FisInterfaceReqBody body;

    @Data
    public static class FisInterfaceReqBody extends FisMsgBody {

        String eqpId;
        String fileType;
        String filePath;
        String fileName;

    }
}
