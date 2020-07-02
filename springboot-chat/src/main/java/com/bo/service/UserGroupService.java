package com.bo.service;

import com.bo.mapper.UserGroupMapper;
import com.bo.pojo.GroupRequest;
import com.bo.pojo.User;
import com.bo.pojo.UserGroup;
import com.bo.utils.IdWorker;
import com.bo.vo.GroupVo;
import com.bo.vo.GroupVo2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserGroupService {
    @Autowired
    private UserGroupMapper userGroupMapper;
    @Autowired
    private IdWorker idWorker;


    public List<GroupVo> selectUserGroupByUid(String uid) {
        return userGroupMapper.selectUserGroupByUid(uid);
    }

    public Integer addUserGroup(Long uid, Long groupId) {
        UserGroup userGroup = new UserGroup(idWorker.nextId(),uid,groupId);
        return userGroupMapper.addUserGroup(userGroup);
    }

    public List<String> selectUserIdByGroupId(String id) {
        return userGroupMapper.selectUserIdByGroupId(id);
    }

    public int selectMembersById(String id) {
        return userGroupMapper.selectMembersById(id);
    }

    public List<User> selectUsersInfoById(String id) {
        return userGroupMapper.selectUsersInfoById(id);
    }

    public UserGroup selectUserGroupByUidGroupId(Long myId, Long groupId) {
        return userGroupMapper.selectUserGroupByUidGroupId(myId, groupId);
    }

    public List<GroupVo2> selectUserGroupVoByUid(String id) {
        List<GroupVo2> groupVo2List = userGroupMapper.selectUserGroupVoByUid(id);
        groupVo2List.forEach(o->o.setType("group"));
        return groupVo2List;
    }
}
