package com.abs.cmn.fis.util.vo;

import lombok.Data;


@Data
public class MessageSendResultVo {

    String trackingKey;
    MessageSendRequestVo messageSendRequestVo;
    String targetTopic;
    String messageSendTime;

    boolean successYn;
}
