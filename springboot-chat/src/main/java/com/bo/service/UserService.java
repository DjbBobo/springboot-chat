package com.bo.service;

import com.bo.exception.CMSException;
import com.bo.mapper.UsersMapper;
import com.bo.pojo.User;
import com.bo.pojo.UserFriendGroup;
import com.bo.redis.GroupKey;
import com.bo.redis.ResultVoKey;
import com.bo.redis.UserKey;
import com.bo.result.ResultCodeEnum;
import com.bo.utils.JsonUtils;
import com.bo.utils.RedisUtil;
import com.bo.vo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private UserFriendGroupService userFriendGroupService;
    @Autowired
    private UserGroupService userGroupService;
    @Autowired
    private GroupsService groupsService;
    @Autowired
    private RedisUtil redisUtil;

    public User login(User user) {
        User tempUser = usersMapper.selectUser(user);
        if (tempUser == null) {
            throw new CMSException(ResultCodeEnum.USER_NOT_EXITST);
        }else if(tempUser.getStatus().equals("forbidden")){
            throw new CMSException(ResultCodeEnum.USER_WAS_FORBIDDEN);
        }

        return tempUser;
    }


    public String init(String uid) {

        // 查询数据库
        List<FriendVo> friend = new ArrayList<>();
        // 查询本人信息
        User mine = null;
        // 查缓存
        String mineStr = (String) redisUtil.get(UserKey.userId, uid);
        if (null != mineStr && mineStr.length() > 0){
            mine = JsonUtils.jsonToPojo(mineStr, User.class);
        }else{
            // 走数据库
            mine = usersMapper.selectUserById(uid);
            mine.setStatus("online");
            // 存进缓存
            redisUtil.set(UserKey.userId, uid, JsonUtils.objectToJson(mine));
        }
        // 查询本人所有的分组以及每个分组的好友
        List<UserFriendGroup> userFriendGroups = userFriendGroupService.selectUserFriendGroupsById(uid);
        for (UserFriendGroup userFriendGroup : userFriendGroups) {
            List<User> users = usersMapper.selectUserByUidGroupId(uid, userFriendGroup.getId());
            //过滤在线人数
            Long onlineCount = 0L;
            if (null != users) { //拥有好友
                onlineCount = users.stream().filter(u -> u.getFakestatus() == 0).count();
                //按 在线->离线->用户名排序
                users = users.stream()
                        .sorted(Comparator.comparing(User::getUsername))
                        .sorted(Comparator.comparing(User::getFakestatus))
                        .collect(Collectors.toList());
                System.out.println("好友列表:" + users.toString());
            }
            friend.add(new FriendVo(userFriendGroup.getGroupname(), userFriendGroup.getId(), onlineCount, users));
        }
        // 查询加入的群组
        List<GroupVo> groups = null;
        String groupStr = (String) redisUtil.get(GroupKey.groupUid, uid);
        if (null != groupStr && groupStr.length() > 0){
            groups = JsonUtils.jsonToList(groupStr,GroupVo.class);
        }else{
            groups = userGroupService.selectUserGroupByUid(uid);
            redisUtil.set(GroupKey.groupUid, uid, JsonUtils.objectToJson(groups));
        }


        // 组装Json
        InitDataVo initDataVo = new InitDataVo(mine, friend, groups);
        ResultVo resultVo = new ResultVo(0, "", initDataVo);
        String resultVoJson = JsonUtils.objectToJson(resultVo);
        return resultVoJson;
    }

    public String getMembers(String id) {
        // 1.查询群主信息
        User owner = groupsService.selectOwnerById(id);
        // 2.查询群组成员数
        int members = userGroupService.selectMembersById(id);
        // 3.查询群组的所有成员的个人信息
        List<User> userList = userGroupService.selectUsersInfoById(id);

        // 4.封装JSON对象
        GroupMembersVo groupMembersVo = new GroupMembersVo(owner, members, userList);
        ResultVo resultVo = new ResultVo(0, "", groupMembersVo);
        return JsonUtils.objectToJson(resultVo);
    }

    public String searchFriend(String account) {
        User user = usersMapper.searchFriend(account);
        if (null != user) {
            List<User> users = new ArrayList<>();
            users.add(user);
            return JsonUtils.objectToJson(users);
        }
        throw new CMSException(ResultCodeEnum.USER_NOT_EXITST);
    }

    public User selectUserById(Long myId) {
        return usersMapper.selectUserById(String.valueOf(myId));
    }


    public Integer updateUserStatusToOnline(String mineId) {
        return usersMapper.updateUserStatusToOnline(mineId);
    }

    public Integer updateUserStatusToOffline(String id) {
        return usersMapper.updateUserStatusToOffline(id);
    }

    public Integer updateUserTrueStatusToOffline(String id) {
        return usersMapper.updateUserTrueStatusToOffline(id);
    }

    public List<User> selectMyOnlineFriend(String mineId) {
        return usersMapper.selectMyOnlineFriend(mineId);
    }


    public List<ReFreshFriendVo> getFriendList(String id) {
        List<ReFreshFriendVo> friends = new ArrayList<>();
        // 查询本人所有的分组以及每个分组的好友
        List<UserFriendGroup> userFriendGroups = userFriendGroupService.selectUserFriendGroupsById(id);
        for (UserFriendGroup userFriendGroup : userFriendGroups) {
            List<User> users = usersMapper.selectUserByUidGroupId(id, userFriendGroup.getId());
            if (null != users) { //拥有好友
                //按 在线->离线->用户名排序
                users = users.stream()
                        .sorted(Comparator.comparing(User::getUsername))
                        .sorted(Comparator.comparing(User::getFakestatus))
                        .collect(Collectors.toList());
                users.forEach(user ->{
                    if (user.getFakestatus() == 1)
                        friends.add(
                                new ReFreshFriendVo(
                                        "friend",
                                        user.getAvatar(),
                                        user.getUsername(),
                                        userFriendGroup.getId(),
                                        user.getId().toString(),
                                        user.getSign(),
                                        "offline",
                                        user.getAccount()));
                    else
                        friends.add(
                                new ReFreshFriendVo(
                                        "friend",
                                        user.getAvatar(),
                                        user.getUsername(),
                                        userFriendGroup.getId(),
                                        user.getId().toString(),
                                        user.getSign(),
                                        user.getStatus(),
                                        user.getAccount()));

                });
            }
        }
        return friends;
    }


    public Integer updateUserTrueStatusToOnline(String id) {
        return usersMapper.updateUserTrueStatusToOnline(id);
    }

    public Integer updateUserSign(String id, String sign) {
        return usersMapper.updateUserSign(id,sign);
    }

    public PageInfo<User> seleAllUser(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<User> userList = usersMapper.seleAllUser();
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        return pageInfo;
    }

    public Integer resetUserPwd(User user) {
        return usersMapper.resetUserPwd(user);
    }

    public Integer forbiddenUser(User user) {
        return usersMapper.forbiddenUser(user);
    }

    public Integer clearForbiddenUser(User user) {
        return usersMapper.clearForbiddenUser(user);
    }

    public Integer updateUserStatusToForbidden(String id) {
        return usersMapper.updateUserStatusToForbidden(id);
    }

    public Integer addQQUser(User user) {
        return usersMapper.addQQUser(user);
    }

    public Integer updateQQUser(User user) {
        return usersMapper.updateQQUser(user);
    }


    public User getQQUserByAccount(String account) {
        return usersMapper.getQQUserByAccount(account);
    }
}
