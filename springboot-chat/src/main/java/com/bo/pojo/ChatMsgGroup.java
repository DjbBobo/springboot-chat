package com.bo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMsgGroup {
    private String id;
    private Long groupId;
    private Long from;
    private String content;
    private Long time;
}
