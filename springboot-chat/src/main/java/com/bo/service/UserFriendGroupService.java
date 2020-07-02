package com.bo.service;

import com.bo.mapper.UserFriendGroupMapper;
import com.bo.pojo.UserFriendGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserFriendGroupService {

    @Autowired
    private UserFriendGroupMapper userFriendGroupMapper;

    public List<UserFriendGroup> selectUserFriendGroupsById(String uid) {
        return userFriendGroupMapper.selectUserFriendGroupsById(uid);
    }

    public Integer addUserFriendGroup(String id, String groupname) {
        return userFriendGroupMapper.addUserFriendGroup(id, groupname);
    }
}
