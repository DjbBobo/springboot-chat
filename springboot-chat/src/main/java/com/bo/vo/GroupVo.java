package com.bo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupVo {
    private Long id;
    private Long ownerUid;
    private String groupname;
    private String avatar;
    private String sign;
}
