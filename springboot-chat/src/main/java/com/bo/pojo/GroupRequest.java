package com.bo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupRequest {
    private Long id;
    private Long senderId;
    private Long groupId;
    private String remark;
    private String time;
    private Integer status;
}
