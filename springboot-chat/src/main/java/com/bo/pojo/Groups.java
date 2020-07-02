package com.bo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Groups {
    private Long id;
    private String groupname;
    private String avatar;
    private Long ownerUid;
    private String sign;
}
