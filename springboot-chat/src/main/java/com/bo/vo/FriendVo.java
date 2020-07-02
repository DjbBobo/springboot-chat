package com.bo.vo;

import com.bo.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendVo {
    private String groupname;
    private Long id;
    private Long online;
    private List<User> list;
}
