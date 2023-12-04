package com.abs.cmn.fis.message.vo.send;

import com.abs.cmn.fis.message.vo.FisMsgCommonVo;
import com.abs.cmn.fis.message.vo.common.FisMsgBody;
import lombok.Data;

@Data
public class BrsInspDataSaveReqVo extends FisMsgCommonVo {

    BrsInspDataSaveReqBody body;

    @Data
    public static class BrsInspDataSaveReqBody extends FisMsgBody {
        String workId;
    }
}
