package com.bo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequest {
    private Long id;
    private Long myId;
    private Long friendId;
    private Long groupId;
    private String remark;
    private Integer status;
    private String time;
    private Integer type;
}
