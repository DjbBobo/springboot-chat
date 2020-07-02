package com.bo.service;

import com.bo.mapper.UserGroupRelMapper;
import com.bo.pojo.User;
import com.bo.pojo.UserGroupRel;
import com.bo.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserGroupRelService {
    @Autowired
    private UserGroupRelMapper userGroupRelMapper;
    @Autowired
    private IdWorker idWorker;

    public int selectMembersById(String id) {
        return userGroupRelMapper.selectMembersById(id);
    }

    public List<User> selectUsersInfoById(String id) {
        return userGroupRelMapper.selectUsersInfoById(id);
    }


    public Integer addUserGroupRel(Long uid, Long groupId) {
        UserGroupRel userGroupRel = new UserGroupRel(idWorker.nextId(),groupId,uid);
        return userGroupRelMapper.addUserGroupRel(userGroupRel);
    }
}
