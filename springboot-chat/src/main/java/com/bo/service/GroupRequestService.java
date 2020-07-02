//package com.bo.service;
//
//import com.bo.exception.CMSException;
//import com.bo.mapper.GroupRequestMapper;
//import com.bo.pojo.FriendRequest;
//import com.bo.pojo.GroupRequest;
//import com.bo.pojo.UserGroup;
//import com.bo.result.ResultCodeEnum;
//import com.bo.utils.IdWorker;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//@Transactional
//public class GroupRequestService {
//    @Autowired
//    private GroupRequestMapper groupRequestMapper;
//    @Autowired
//    private UserGroupService userGroupService;
//    @Autowired
//    private IdWorker idWorker;
//
//
//    public boolean sendGroupRequest(GroupRequest groupRequest) {
//        // 不能重复添加群
//        UserGroup userGroup = userGroupService.selectUserGroupByGroupRequest(groupRequest);
//        if (null != userGroup){
//            throw new CMSException(ResultCodeEnum.REPEAT_ADD_GROUP_REQUEST);
//        }
//        // 不能重复发送请求
//        Integer count = groupRequestMapper.selectUnReadRequestByGroupRequest(groupRequest);
//        if (count > 0){
//            throw new CMSException(ResultCodeEnum.REPEAT_GROUP_REQUEST);
//        }
//        // 插入请求到数据库
//        groupRequest.setId(idWorker.nextId());
//        count = groupRequestMapper.insertGroupRequest(groupRequest);
//        if (count > 0){
//            return true;
//        }
//        return false;
//    }
//
//    public Integer getUnReadGroupRequestNum(Long id) {
//        return groupRequestMapper.getUnReadGroupRequestNum(id);
//    }
//
//    public List<FriendRequest> getGroupRequest(String id) {
//        return groupRequestMapper.getGroupRequest(id);
//    }
//
//    public void setGroupRequestRead(String id) {
//        groupRequestMapper.setGroupRequestRead(id);
//    }
//}
