package com.bo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupRel {
    private Long id;
    private Long groupId;
    private Long uid;
}
