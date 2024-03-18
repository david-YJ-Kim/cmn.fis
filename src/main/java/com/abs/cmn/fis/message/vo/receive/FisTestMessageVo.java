package com.abs.cmn.fis.message.vo.receive;

import com.abs.cmn.fis.message.vo.FisMsgCommonVo;
import com.abs.cmn.fis.message.vo.common.FisMsgBody;

import lombok.Data;

@Data
public class FisTestMessageVo extends FisMsgCommonVo {

    FisTestMessageBody body;

    @Data
    public static class FisTestMessageBody extends FisMsgBody{

        Long sleepTm;
    }
}
