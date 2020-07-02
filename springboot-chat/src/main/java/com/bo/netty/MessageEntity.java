package com.bo.netty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageEntity {
    private String id;
    private String username;
    private String fromid;
    private String avatar;
    private String type;
    private String content;
    private String cid;
    private Boolean mine;
    private Long timestamp;
}
