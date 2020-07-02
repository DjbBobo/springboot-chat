package com.bo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReFreshFriendVo {
    private String type;
    private String avatar;
    private String username;
    private Long groupid;
    private String id;
    private String sign;
    private String status;
    private String account;
}
