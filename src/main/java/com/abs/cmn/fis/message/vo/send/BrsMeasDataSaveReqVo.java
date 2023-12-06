package com.abs.cmn.fis.message.vo.send;

import com.abs.cmn.fis.message.vo.FisMsgCommonVo;
import com.abs.cmn.fis.message.vo.common.FisMsgBody;
import lombok.Data;

@Data
public class BrsMeasDataSaveReqVo extends FisMsgCommonVo {

    BrsMeasDataSaveReqBody body;

    @Data
    public static class BrsMeasDataSaveReqBody extends FisMsgBody {
        String workId;
    }
}
