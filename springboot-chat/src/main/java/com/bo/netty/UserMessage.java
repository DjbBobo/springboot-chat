package com.bo.netty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserMessage {
    private String type;
    private String avatar;
    private String username;
    private Long groupid;
    private Long id;
    private String sign;
}
