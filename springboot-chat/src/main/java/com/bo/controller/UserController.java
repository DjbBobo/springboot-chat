package com.bo.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.bo.netty.MessageEntity;
import com.bo.pojo.*;
import com.bo.redis.GroupKey;
import com.bo.result.R;
import com.bo.service.*;
import com.bo.utils.FastDFSClient;
import com.bo.utils.JsonUtils;
import com.bo.utils.RedisUtil;
import com.bo.vo.*;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    private static final String filePrefixUrl = "http://39.97.241.159:88/layim/";

    @Autowired
    private UserService userService;
    @Autowired
    private FastDFSClient fastDFSClient;
    @Autowired
    private FriendRequestService friendRequestService;
    @Autowired
    private FriendGroupRelService friendGroupRelService;
    @Autowired
    private ChatMsgFriendService chatMsgFriendService;
    @Autowired
    private GroupsService groupsService;
    @Autowired
    private UserGroupService userGroupService;
    @Autowired
    private GroupMsgSignService groupMsgSignService;
    @Autowired
    private ChatMsgGroupService chatMsgGroupService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private RedisUtil redisUtil;


    /**
     * 用户登陆功能
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public R login(@RequestBody User user) {
        System.out.println(user.toString());
        return R.ok().data("mine", userService.login(user));
    }

    /**
     * 用户数据初始化
     *
     * @return
     */
    @RequestMapping(value = "/init/{uid}", method = RequestMethod.GET)
    public String init(@PathVariable String uid) {
        return userService.init(uid);
    }

    /**
     * 获取群组的成员
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/getMembers", method = RequestMethod.GET)
    public String getMembers(@RequestParam String id) {
        return userService.getMembers(id);
    }

    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    public String uploadImage(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        // 上传文件到服务器，得到URL
        String url = fastDFSClient.uploadBase64(file);
        //拼接完整的URL
        url = filePrefixUrl + url;
        System.out.println(url);
        //xxxxxxxxxxxxx.png
        //xxxxxxxxxxxxx_80x80.png
        FileVo fileVo = new FileVo(url, "");

        return JsonUtils.objectToJson(new ResultVo(0, "", fileVo));
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public String uploadFile(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        // 上传文件到服务器，得到URL
        String url = fastDFSClient.uploadFile(file);
        //拼接完整的URL
        url = filePrefixUrl + url;
        System.out.println(url);
        FileVo fileVo = new FileVo(url, file.getOriginalFilename());

        return JsonUtils.objectToJson(new ResultVo(0, "", fileVo));
    }

    /**
     * 通过account查找用户信息
     *
     * @param account
     * @return
     */
    @RequestMapping(value = "/searchFriend/{account}", method = RequestMethod.GET)
    public R searchFriend(@PathVariable("account") String account) {
        String userJson = userService.searchFriend(account);
        return R.ok().data("item", userJson);
    }

    @RequestMapping(value = "/searchGroup/{groupid}", method = RequestMethod.GET)
    public R searchGroup(@PathVariable("groupid") String groupid) {
        String group = groupsService.searchGroup(groupid);
        return R.ok().data("item", group);
    }


    /**
     * 发送申请好友请求
     *
     * @param friendRequest
     * @return
     */
    @RequestMapping(value = "/sendFriendRequest", method = RequestMethod.POST)
    public R sendFriendRequest(@RequestBody FriendRequest friendRequest) {
        boolean flag = friendRequestService.sendFriendRequest(friendRequest);
        if (flag) {
            return R.ok().message("申请成功");
        }
        return R.error().message("申请失败");
    }

    /**
     * 发送申请群组请求
     *
     * @param friendRequest
     * @return
     */
    @RequestMapping(value = "/sendFriendGroupRequest", method = RequestMethod.POST)
    public R sendFriendGroupRequest(@RequestBody FriendRequest friendRequest) {
        boolean flag = friendRequestService.sendFriendGroupRequest(friendRequest);
        if (flag) {
            return R.ok().message("申请成功");
        }
        return R.error().message("申请失败");
    }


    /**
     * 获取申请好友列表的请求
     *
     * @param page
     * @param id
     * @return
     */
    @RequestMapping(value = "/getFriendGroupRequest", method = RequestMethod.GET)
    public String getFriendGroupRequest(@RequestParam("page") Integer page,
                                        @RequestParam(value = "count", defaultValue = "8") Integer size,
                                        @RequestParam("id") String id) throws ParseException {
        String listJson = friendRequestService.getFriendGroupRequest(page, size, id);
        return listJson;
    }

    /**
     * 获取消息盒子未读信息数
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/getUnReadRequestNum/{id}", method = RequestMethod.GET)
    public R getUnReadRequestNum(@PathVariable("id") Long id) {
        Integer count = friendRequestService.getUnReadRequestNum(id);
        return R.ok().data("msgNum", count);
    }

    /**
     * 同意好友请求
     *
     * @param myId
     * @param uid
     * @param from_group
     * @param group
     * @return
     */
    @RequestMapping(value = "/agreeFriend", method = RequestMethod.POST)
    public R agreeFriend(@RequestParam("myId") String myId,
                         @RequestParam("uid") String uid,
                         @RequestParam("from_group") String from_group,
                         @RequestParam("group") String group) {
        System.out.println(myId + "," + uid + "," + from_group + "," + group);
        // 1.修改请求表的状态为：2:同意
        Integer count = friendRequestService.updateStausAgree(myId, uid);
        // 2.添加好友记录
        Integer res = friendGroupRelService.insertFriend(myId, from_group, uid, group);
        System.out.println(count + "=============" + res);
        if (count == 1 && res == 1) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 改变在线状态
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/setUserStatusToOnline", method = RequestMethod.POST)
    public R setUserStatusToOnline(@RequestParam("id") String id) {
        Integer count = userService.updateUserStatusToOnline(id);
        if (count > 0)
            return R.ok();
        return R.error();
    }

    /**
     * 改变离线状态
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/setStatusToOffline", method = RequestMethod.POST)
    public R setStatusToOffline(@RequestParam("id") String id) {
        Integer count = userService.updateUserStatusToOffline(id);
        if (count > 0)
            return R.ok();
        return R.error();
    }

    /**
     * 获取好友列表
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/getFriendList/{id}", method = RequestMethod.GET)
    public R getFriendList(@PathVariable("id") String id) {
        List<ReFreshFriendVo> userList = userService.getFriendList(id);
        return R.ok().data("friends", JsonUtils.objectToJson(userList));
    }

    /**
     * 查询用户的群组
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/getGroupList/{id}", method = RequestMethod.GET)
    public R getGroupList(@PathVariable("id") String id) {
        redisUtil.del(GroupKey.groupUid, id);
        List<GroupVo2> groupVoList = userGroupService.selectUserGroupVoByUid(id);
        return R.ok().data("groups", JsonUtils.objectToJson(groupVoList));
    }

    /**
     * 签收消息
     *
     * @param from
     * @param to
     * @return
     */
    @RequestMapping(value = "/signFriendMsg", method = RequestMethod.POST)
    public R signFriendMsg(@RequestParam("from") String from, @RequestParam("to") String to) {
        Integer count = chatMsgFriendService.signMsg(from, to);
        if (count > 0) {
            return R.ok().message("签收成功");
        }
        return R.error().message("签收失败");
    }

    /**
     * 签收消息
     *
     * @param groupMsgId
     * @param to
     * @return
     */
    @RequestMapping(value = "/signGroupMsg", method = RequestMethod.POST)
    public R signGroupMsg(@RequestParam("group_msg_id") String groupMsgId, @RequestParam("to") String to) {
        System.out.println(groupMsgId + "===" + to);
        Integer count = groupMsgSignService.signMsg(groupMsgId, to);
        if (count > 0) {
            return R.ok().message("签收成功");
        }
        return R.error().message("签收失败");
    }

    @RequestMapping(value = "/getUnSignFriendMsg/{id}", method = RequestMethod.GET)
    public R getUnSignFriendMsg(@PathVariable("id") String to) {
        List<MessageEntity> chatMsgFriendList = chatMsgFriendService.selectUnSignMsg(to);
        if (null != chatMsgFriendList) {
            chatMsgFriendList.forEach(msg -> {
                msg.setType("friend");
                msg.setCid("0");
                msg.setMine(false);
            });
            return R.ok().data("messageEntitys", JsonUtils.objectToJson(chatMsgFriendList)).message("查询未签收好友消息");
        }
        return R.ok().message("没有查询到未签收的消息");
    }

    /**
     * 查询未签收的群组消息
     *
     * @param to
     * @return
     */
    @RequestMapping(value = "/getUnSignGroupMsg/{id}", method = RequestMethod.GET)
    public R getUnSignGroupMsg(@PathVariable("id") String to) {
        List<MessageEntity> chatMsgFriendList = groupMsgSignService.selectUnSignMsg(to);
        if (null != chatMsgFriendList) {
            chatMsgFriendList.forEach(msg -> {
                msg.setType("group");
                msg.setMine(false);
            });
            return R.ok().data("messageEntitys", JsonUtils.objectToJson(chatMsgFriendList)).message("查询未签收群组消息");
        }
        return R.ok().message("没有查询到未签收的消息");
    }

    /**
     * 查询好友聊天记录
     *
     * @param uid
     * @param fid
     * @return
     */
    @RequestMapping(value = "/getFriendChatLog/{uid}/{fid}", method = RequestMethod.GET)
    public R getFriendChatLog(@PathVariable("uid") String uid,
                              @PathVariable("fid") String fid) {
        List<ChatLogVo> chatLogVoList = chatMsgFriendService.getFriendChatLog(uid, fid);
        ResultVo resultVo = new ResultVo(0, "", chatLogVoList);
        return R.ok().data("results", JsonUtils.objectToJson(resultVo));
    }

    /**
     * 查询群组聊天记录
     *
     * @param groupId
     * @return
     */
    @RequestMapping(value = "/getGroupChatLog/{groupid}", method = RequestMethod.GET)
    public R getGroupChatLog(@PathVariable("groupid") String groupId) {
        List<ChatLogVo> chatLogVoList = chatMsgGroupService.getGroupChatLog(groupId);
        ResultVo resultVo = new ResultVo(0, "", chatLogVoList);
        return R.ok().data("results", JsonUtils.objectToJson(resultVo));
    }

    /**
     * 新建群组
     *
     * @param ownerId
     * @param avatar
     * @param groupname
     * @param sign
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addGroup", method = RequestMethod.POST)
    public R addGroup(@RequestParam("me") Long ownerId,
                      @RequestParam("avatar") String avatar,
                      @RequestParam("groupname") String groupname,
                      @RequestParam("sign") String sign) throws Exception {

        // 操作Groups
        GroupVo2 group = groupsService.addGroup(ownerId, avatar, groupname, null);
        // 操作user_group
        Integer count = userGroupService.addUserGroup(ownerId, group.getId());
        System.out.println(group);
        System.out.println(count+"=============");
        if (count > 0) {
            redisUtil.del(GroupKey.groupUid, String.valueOf(ownerId));
            return R.ok().message("创建成功").data("group", JsonUtils.objectToJson(group));
        }
        return R.error().message("创建失败，请重新操作");
    }

    /**
     * 添加群成员
     *
     * @param userGroup
     * @return
     */
    @RequestMapping(value = "/addUserGroup", method = RequestMethod.POST)
    public R addUserGroup(@RequestBody UserGroup userGroup) {
        // 添加群成员
        Integer count = userGroupService.addUserGroup(userGroup.getUid(), userGroup.getGroupId());
        // 设置请求状态为 2
        Integer num = friendRequestService.updateGroupStausAgree(userGroup);
        if (count > 0 && num > 0)
            return R.ok();
        return R.error();
    }

    /**
     * 拒绝申请群请求
     *
     * @param uid
     * @param fromGroup
     * @return
     */
    @RequestMapping(value = "/refuseGroupRequest", method = RequestMethod.POST)
    public R refuseGroupRequest(@RequestParam("uid") String uid, @RequestParam("fromGroup") String fromGroup) {
        Integer count = friendRequestService.updateGroupStausRefuse(uid, fromGroup);
        if (count > 0)
            return R.ok();
        return R.error();
    }

    /**
     * 拒绝好友申请
     *
     * @param uid
     * @param myId
     * @return
     */
    @RequestMapping(value = "/refuseFriendRequest", method = RequestMethod.POST)
    public R refuseFriendRequest(@RequestParam("uid") String uid, @RequestParam("myId") String myId) {
        Integer count = friendRequestService.updateFriendStausRefuse(uid, myId);
        if (count > 0)
            return R.ok();
        return R.error();
    }

    /**
     * 修改签名
     *
     * @param id
     * @param sign
     * @return
     */
    @RequestMapping(value = "/updateUserSign", method = RequestMethod.POST)
    public R updateUserSign(@RequestParam("id") String id, @RequestParam("sign") String sign) {
        Integer count = userService.updateUserSign(id, sign);
        if (count > 0)
            return R.ok();
        return R.error();
    }

    @RequestMapping(value = "/adminLogin", method = RequestMethod.POST)
    public R adminLogin(@RequestBody Admin admin) {
        admin = adminService.selectAdmin(admin);
        if (null != admin)
            return R.ok().message("欢迎你").data("admin", JsonUtils.objectToJson(admin));
        return R.error().message("用户名或密码错误");
    }

    @RequestMapping(value = "userList", method = RequestMethod.GET)
    public String userList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                           @RequestParam(value = "limit", defaultValue = "5") Integer size) {
        PageInfo<User> pageInfo = userService.seleAllUser(page, size);
        ConListVo conListVo = new ConListVo(0, "", pageInfo.getTotal(), pageInfo.getList());
        return JsonUtils.objectToJson(conListVo);
    }

    @RequestMapping(value = "groupList", method = RequestMethod.GET)
    public String groupList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                            @RequestParam(value = "limit", defaultValue = "5") Integer size) {
        PageInfo<AllGroupVo> pageInfo = groupsService.selectAllGroup(page, size);
        List<AllGroupVo> list = pageInfo.getList();
        list.forEach(g -> {
            int member = userGroupService.selectMembersById(g.getId());
            g.setMembers(String.valueOf(member));
            g.setStatus("normal");
        });
        ConListVo conListVo = new ConListVo(0, "", pageInfo.getTotal(), list);
        return JsonUtils.objectToJson(conListVo);

    }


    @RequestMapping(value = "/resetUserPwd", method = RequestMethod.POST)
    public R resetUserPwd(@RequestBody User user) {
        System.out.println(user);
        Integer count = userService.resetUserPwd(user);
        if (count > 0)
            return R.ok();
        return R.error();
    }

    @RequestMapping(value = "/forbiddenUser", method = RequestMethod.POST)
    public R forbiddenUser(@RequestBody User user) {
        Integer count = userService.forbiddenUser(user);
        if (count > 0)
            return R.ok();
        return R.error();
    }

    @RequestMapping(value = "/clearForbiddenUser", method = RequestMethod.POST)
    public R clearForbiddenUser(@RequestBody User user) {
        Integer count = userService.clearForbiddenUser(user);
        if (count > 0)
            return R.ok();
        return R.error();
    }


    @RequestMapping(value = "/getAdminFriendList/{id}", method = RequestMethod.GET)
    public R getAdminFriendList(@PathVariable("id") String id) {
        // 查询用户好友的id
        List<Long> ids = friendGroupRelService.selectFidByUid(id);
        if (null == ids) {
            return R.ok().message("暂无好友");
        }
        List<User> friendList = new ArrayList<>();
        ids.forEach(fid -> {
            friendList.add(userService.selectUserById(fid));
        });
        return R.ok().data("friendList", friendList);
    }

    @RequestMapping(value = "/getAdminGroupFriendList/{id}", method = RequestMethod.GET)
    public R getAdminGroupFriendList(@PathVariable("id") String id) {
        List<User> userList = userGroupService.selectUsersInfoById(id);
        return R.ok().data("groupFriendList", userList);
    }

    @RequestMapping(value = "/getQQUserByAccount/{account}",method = RequestMethod.GET)
    public R getQQUserByAccount(@PathVariable("account")String account){
        User user = userService.getQQUserByAccount(account);
        System.out.println(user.toString());
        if (null != user)
            return R.ok().data("mine",user);
        return R.error();
    }





}