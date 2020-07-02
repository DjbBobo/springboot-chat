package com.bo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupVo2 {
    private Long id;
    private String type;
    private String avatar;
    private String groupname;
    private String sign;
}
