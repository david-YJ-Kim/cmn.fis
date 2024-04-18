package com.abs.cmn.fis.util.vo;

import lombok.Builder;
import lombok.Data;

@Data
public class MessageSendRequestVo {

    String eventName;
    String targetSystem;
    String payload;

    @Builder
    public MessageSendRequestVo(String eventName, String targetSystem, String payload) {
        this.eventName = eventName;
        this.targetSystem = targetSystem;
        this.payload = payload;
    }
}
