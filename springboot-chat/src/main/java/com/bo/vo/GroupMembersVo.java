package com.bo.vo;

import com.bo.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMembersVo{
    private User owner;
    private Integer members;
    private List<User> list;
}
