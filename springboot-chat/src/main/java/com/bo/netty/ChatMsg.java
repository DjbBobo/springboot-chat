package com.bo.netty;

import lombok.Data;

@Data
public class ChatMsg {
    private String msgId;
    private String senderId;
    private String receiverId;
    private String content;
}
