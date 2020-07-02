package com.bo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllGroupVo {
    private String id;
    private String groupname;
    private String members;
    private String avatar;
    private String owner;
    private String sign;
    private String status;
}
