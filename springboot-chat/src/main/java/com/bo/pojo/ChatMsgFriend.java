package com.bo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMsgFriend {
    private Long id;
    private Long from;
    private Long to;
    private String content;
    private Long time;
}
