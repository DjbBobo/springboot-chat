package com.bo.vo;

import com.bo.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitDataVo{
    private User mine;
    private List<FriendVo> friend;
    private List<GroupVo> group;
}
