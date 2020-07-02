package com.bo.service;

import com.bo.exception.CMSException;
import com.bo.mapper.FriendRequestMapper;
import com.bo.pojo.*;
import com.bo.result.ResultCodeEnum;
import com.bo.utils.IdWorker;
import com.bo.utils.JsonUtils;
import com.bo.utils.RelativeDateFormat;
import com.bo.vo.FriendRequestDataVo;
import com.bo.vo.FriendRequestVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class FriendRequestService {

    @Autowired
    private IdWorker idWorker;
    @Autowired
    private FriendRequestMapper friendRequestMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private FriendGroupRelService friendGroupRelService;
    @Autowired
    private UserGroupService userGroupService;
    @Autowired
    private GroupsService groupsService;

    public boolean sendFriendRequest(FriendRequest friendRequest) {
        // 1.不能添加自己
        if (friendRequest.getMyId() == friendRequest.getFriendId()){
            throw new CMSException(ResultCodeEnum.CAN_NOT_ADD_SELF);
        }
        // 2.不能重复添加好友
        FriendGroupRel friendGroupRel = friendGroupRelService.selectFriendGroupRelByUidFid(
                                        friendRequest.getMyId(), friendRequest.getFriendId());
        if (null != friendGroupRel){
            throw new CMSException(ResultCodeEnum.IS_EXIST_FRIEND);
        }
        // 3.不能重复发送请求
        FriendRequest tempRequest = friendRequestMapper.selectRequestByUidFid(friendRequest.getMyId(),friendRequest.getFriendId());
        if (null != tempRequest){
            throw new CMSException(ResultCodeEnum.REPEAT_REQUEST);
        }

        // 4. 插入申请数据
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        friendRequest.setId(idWorker.nextId());
        friendRequest.setTime(time);
        Integer count = friendRequestMapper.sendFriendRequest(friendRequest);
        if(count > 0)
            return true;
        return false;
    }

    public String getFriendGroupRequest(Integer page, Integer size, String id) throws ParseException {
        // 1.分页查询
        PageHelper.startPage(page,size);
        // 查询好友、群组请求
        List<FriendRequest> requestList = friendRequestMapper.getFriendGroupRequest(id);

        PageInfo<FriendRequest> pageInfo = new PageInfo<>(requestList);
        // 将未读设置为已读
        friendRequestMapper.setFriendGroupRequestRead(id);
        // 2.得到当前页的数据
        List<FriendRequest> list = pageInfo.getList();
        // 3.遍历组转返回对象
        List<FriendRequestDataVo> friendRequestDataVos = new ArrayList<>();
        for (FriendRequest friendRequest : list) {
            // 3.1 查询对方用户的信息
            User user = null;
            User nullUser = new User();
            // 3.2 格式化日期
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = simpleDateFormat.parse(friendRequest.getTime());
            String time = RelativeDateFormat.format(date);

            // 好友申请
            if(friendRequest.getType() == 0){
                // 别人申请我
                if (friendRequest.getFriendId().equals(Long.valueOf(id))){
                    user = userService.selectUserById(friendRequest.getMyId());
                    // 接收|拒绝
                    if (friendRequest.getStatus() == 0 || friendRequest.getStatus() == 1){
                        System.out.println("接收|拒绝");
                        friendRequestDataVos.add(
                                new FriendRequestDataVo(
                                        friendRequest.getId(),"申请添加你为好友",
                                        friendRequest.getId(),friendRequest.getMyId(),
                                        friendRequest.getGroupId(), 1,
                                        friendRequest.getRemark(),null,1,
                                        time, user,0)
                        );
                    }else if(friendRequest.getStatus() == 2){
                        //已同意
                        friendRequestDataVos.add(
                                new FriendRequestDataVo(
                                        friendRequest.getId(),"申请添加你为好友",
                                        friendRequest.getId(),friendRequest.getMyId(),
                                        friendRequest.getGroupId(), 2,
                                        friendRequest.getRemark(),null,1,
                                        time, user,0)
                        );
                    }else if (friendRequest.getStatus() == 3){
                        // 已拒绝
                        friendRequestDataVos.add(
                                new FriendRequestDataVo(
                                        friendRequest.getId(),"申请添加你为好友",
                                        friendRequest.getId(),friendRequest.getMyId(),
                                        friendRequest.getGroupId(), 3,
                                        friendRequest.getRemark(),null,1,
                                        time, user,0)
                        );
                    }
                }else if (friendRequest.getMyId().equals(Long.valueOf(id))){
                    user = userService.selectUserById(friendRequest.getFriendId());
                    // 我申请别人
                    // 已申请
                    if (friendRequest.getStatus() == 0 || friendRequest.getStatus() == 1){
                        friendRequestDataVos.add(
                                new FriendRequestDataVo(
                                        friendRequest.getId(),"申请添加对方为好友",
                                        friendRequest.getId(),friendRequest.getMyId(),
                                        friendRequest.getGroupId(), 4,
                                        friendRequest.getRemark(),null,1,
                                        time, user,0)
                        );
                    }else if (friendRequest.getStatus() == 2){
                        // 3.4 别人同意
                        friendRequestDataVos.add(
                                new FriendRequestDataVo(
                                        friendRequest.getId(),user.getUsername()+" 已经同意你的好友申请",
                                        friendRequest.getId(),null,
                                        null,1,null,
                                        null,1,time, nullUser,0
                                )
                        );
                    }else{
                        // 3.5 拒绝
                        friendRequestDataVos.add(
                                new FriendRequestDataVo(
                                        friendRequest.getId(),user.getUsername()+" 拒绝了你的好友申请",
                                        friendRequest.getId(),null,
                                        null,1,
                                        null,null,
                                        1,time, nullUser,0
                                )
                        );
                    }
                }

            }else{//群组申请
                // 别人申请我的群
                if (friendRequest.getFriendId().equals(Long.valueOf(id))){
                    // 申请人信息
                    user = userService.selectUserById(friendRequest.getMyId());
                    // 群信息
                    Groups group = groupsService.selectGroup(friendRequest.getGroupId());
                    if (friendRequest.getStatus() == 0 || friendRequest.getStatus() == 1){
                        friendRequestDataVos.add(
                                new FriendRequestDataVo(
                                        friendRequest.getId(),"申请加入群 <span style='color:#0000CC;'>"+group.getGroupname()+"</span>",
                                        friendRequest.getId(),friendRequest.getMyId(),
                                        friendRequest.getGroupId(), 1,
                                        friendRequest.getRemark(),null,1,
                                        time, user,1)
                        );
                    }else if(friendRequest.getStatus() == 2){
                        //已同意
                        friendRequestDataVos.add(
                                new FriendRequestDataVo(
                                        friendRequest.getId(),"申请加入群 <span style='color:#0000CC;'>"+group.getGroupname()+"</span>",
                                        friendRequest.getId(),friendRequest.getMyId(),
                                        friendRequest.getGroupId(), 2,
                                        friendRequest.getRemark(),null,1,
                                        time, user,1)
                        );
                    }else if (friendRequest.getStatus() == 3){
                        // 已拒绝
                        friendRequestDataVos.add(
                                new FriendRequestDataVo(
                                        friendRequest.getId(),"申请加入群 <span style='color:#0000CC;'>"+group.getGroupname()+"</span>",
                                        friendRequest.getId(),friendRequest.getMyId(),
                                        friendRequest.getGroupId(), 3,
                                        friendRequest.getRemark(),null,1,
                                        time, user,1)
                        );
                    }
                }else{// 我申请别人的群
                    user = userService.selectUserById(friendRequest.getFriendId());
                    // 群信息
                    Groups group = groupsService.selectGroup(friendRequest.getGroupId());
                    // 已申请
                    if (friendRequest.getStatus() == 0 || friendRequest.getStatus() == 1){
                        friendRequestDataVos.add(
                                new FriendRequestDataVo(
                                        friendRequest.getId(),"申请加入群 <span style='color:#0000CC;'>"+group.getGroupname()+"</span>",
                                        friendRequest.getFriendId(),friendRequest.getFriendId(),
                                        friendRequest.getGroupId(), 4,
                                        friendRequest.getRemark(),null,1,
                                        time, group,1)
                        );
                    }else if (friendRequest.getStatus() == 2){
                        // 3.4 别人同意
                        friendRequestDataVos.add(
                                new FriendRequestDataVo(
                                        friendRequest.getId(),"群 <span style='color:#0000CC;'>"+group.getGroupname()+"</span> 管理员同意了你的加群申请",
                                        friendRequest.getFriendId(),null,
                                        null,1,null,
                                        null,1,time, nullUser,1
                                )
                        );
                    }else{
                        // 3.5 拒绝
                        friendRequestDataVos.add(
                                new FriendRequestDataVo(
                                        friendRequest.getId(),"群 <span style='color:#0000CC;'>"+group.getGroupname()+"</span> 管理员拒绝了你的加群申请",
                                        friendRequest.getFriendId(),null,
                                        null,1,
                                        null,null,
                                        1,time, nullUser,1
                                )
                        );
                    }
                }

            }

        }
        FriendRequestVo requestVo = new FriendRequestVo(0, pageInfo.getPages(), friendRequestDataVos);
        System.out.println(requestVo);
        return JsonUtils.objectToJson(requestVo);
    }

    public Integer getUnReadRequestNum(Long id) {
        return friendRequestMapper.getUnReadRequestNum(id);
    }

    public Integer updateStausAgree(String myId, String uid) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        return friendRequestMapper.updateStausAgree(myId,uid,time);
    }

    public boolean sendFriendGroupRequest(FriendRequest friendRequest) {
        if (friendRequest.getType() == 0){
            // 1.不能添加自己
            if (friendRequest.getMyId() == friendRequest.getFriendId()){
                throw new CMSException(ResultCodeEnum.CAN_NOT_ADD_SELF);
            }
            // 2.不能重复添加好友
            FriendGroupRel friendGroupRel = friendGroupRelService.selectFriendGroupRelByUidFid(
                    friendRequest.getMyId(), friendRequest.getFriendId());
            if (null != friendGroupRel){
                throw new CMSException(ResultCodeEnum.IS_EXIST_FRIEND);
            }
            // 3.不能重复发送请求
            Integer count = friendRequestMapper.selectUnReadRequestByUidGroupId(friendRequest.getMyId(),friendRequest.getFriendId(),friendRequest.getType());
            if (count > 0){
                throw new CMSException(ResultCodeEnum.REPEAT_REQUEST);
            }
        }else{
            // 不能重复添加群
            UserGroup userGroup = userGroupService.selectUserGroupByUidGroupId(friendRequest.getMyId(),friendRequest.getGroupId());
            if (null != userGroup){
                throw new CMSException(ResultCodeEnum.REPEAT_ADD_GROUP_REQUEST);
            }
            // 不能重复发送请求
            Integer count = friendRequestMapper.selectUnReadRequestByUidGroupId(friendRequest.getMyId(),friendRequest.getGroupId(),friendRequest.getType());
            if (count > 0){
                throw new CMSException(ResultCodeEnum.REPEAT_GROUP_REQUEST);
            }
        }
        // 插入请求到数据库
        friendRequest.setId(idWorker.nextId());
        Integer count = friendRequestMapper.sendFriendGroupRequest(friendRequest);
        if (count > 0)
            return true;
        return false;
    }

    public Integer updateGroupStausAgree(UserGroup userGroup) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        return friendRequestMapper.updateGroupStausAgree(userGroup.getUid(),userGroup.getGroupId(),time);
    }

    public Integer updateGroupStausRefuse(String uid, String fromGroup) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        return friendRequestMapper.updateGroupStausRefuse(uid,fromGroup,time);
    }

    public Integer updateFriendStausRefuse(String myId, String friendId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        return friendRequestMapper.updateFriendStausRefuse(myId,friendId,time);
    }
}
