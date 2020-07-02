package com.bo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatLogVo {
    private String username;
    private Long id;
    private String avatar;
    private Long timestamp;
    private String content;
}
