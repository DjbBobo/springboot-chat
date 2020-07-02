package com.bo.netty;

import com.bo.SpringUtil;
import com.bo.enums.MsgActionEnum;
import com.bo.exception.CMSException;
import com.bo.pojo.ChatMsgFriend;
import com.bo.pojo.User;
import com.bo.result.ResultCodeEnum;
import com.bo.service.*;
import com.bo.utils.ChannelWriteAndFlushUtil;
import com.bo.utils.IdWorker;
import com.bo.utils.JsonUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * 处理消息的Handler
 * TextWebSocketFrame：在netty中，是用于websocket专门处理文本的对象，frame是消息的载体
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static UserGroupService userGroupService;
    private static UserService userService;
    private static ChatMsgFriendService chatMsgFriendService;
    private static ChatMsgGroupService chatMsgGroupService;
    private static GroupMsgSignService groupMsgSignService;
    private static IdWorker idWorker;
    private static Lock lock = new ReentrantLock();

    // 用于记录和管理所有客户端的channel
    public static ChannelGroup users =
            new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        // 获取客户端传输过来的消息
        String content = msg.text();

        Channel currentChannel = ctx.channel();

        // 1.获取客户端发来的消息
        DataContent dataContent = JsonUtils.jsonToPojo(content, DataContent.class);
        System.out.println(dataContent.toString());
        Integer type = dataContent.getType();
        // 2.判断消息类型，根据不同的类型来处理不同的业务
        if (type == MsgActionEnum.CONNECT.type) {
            // 当websocket第一次open的时候，初始化channel，把用户的channel和userid关联起来
            Mine mine = dataContent.getMine();
            UserChannelRel.put(mine.getId(), currentChannel);
            // 上线通知：修改为在线状态并通知我的在线好友
            userService = SpringUtil.getBean(UserService.class);
            try {
                lock.lock();
                userService.updateUserTrueStatusToOnline(mine.getId());
                userService.updateUserStatusToOnline(mine.getId());
            } catch (Exception e) {
                throw new CMSException(ResultCodeEnum.UNKNOWN_ERROR);
            } finally {
                lock.unlock();
            }
            List<User> userList = userService.selectMyOnlineFriend(mine.getId());
            System.out.println(userList);
            if (null != userList)
                userList.forEach(user ->
                        ChannelWriteAndFlushUtil.writeAndFlush(user.getId().toString(), users, mine, MsgActionEnum.ONLINE.type));


        } else if (type == MsgActionEnum.CHAT.type) {
            // 接收到的消息
            Mine mine = dataContent.getMine();
            To to = dataContent.getTo();
            Long timestamp = System.currentTimeMillis();
            // 封装消息对象
            MessageEntity messageEntity =
                    new MessageEntity(
                            mine.getId(), mine.getUsername(),
                            mine.getId(), mine.getAvatar(), to.getType(),
                            mine.getContent(), "0", !mine.getMine(), timestamp);
            // 判断是单聊 / 群聊
            if (to.getType().equals("friend")) {
                // 单聊
                // 消息插入数据库
                idWorker = SpringUtil.getBean(IdWorker.class);
                chatMsgFriendService = SpringUtil.getBean(ChatMsgFriendService.class);
                chatMsgFriendService.insertMsg(new ChatMsgFriend(idWorker.nextId(), Long.valueOf(mine.getId()), Long.valueOf(to.getId()), mine.getContent(), timestamp));
                ChannelWriteAndFlushUtil.writeAndFlush(to.getId(), users, messageEntity, MsgActionEnum.CHAT.type);
            } else {
                // 群聊

                //设置群聊id
                messageEntity.setId(to.getId());
                // 查询群组下的所有成员ID
                userGroupService = SpringUtil.getBean(UserGroupService.class);
                List<String> ids = userGroupService.selectUserIdByGroupId(to.getId())
                        .stream().filter(id -> !id.equals(mine.getId())).collect(Collectors.toList());
                // 聊天记录插入数据库
                idWorker = SpringUtil.getBean(IdWorker.class);
                String groupMsgId = String.valueOf(idWorker.nextId());
                chatMsgGroupService = SpringUtil.getBean(ChatMsgGroupService.class);
                chatMsgGroupService.insertMsg(groupMsgId, to.getId(), mine.getId(), mine.getContent(), System.currentTimeMillis());

                groupMsgSignService = SpringUtil.getBean(GroupMsgSignService.class);
                ids.forEach(id -> {
                    groupMsgSignService.insertMsg(idWorker.nextId(), groupMsgId, id);
                });

                messageEntity.setCid(groupMsgId);

                System.out.println("该群组下的所有成员ID：" + ids.toString());
                // 遍历Channel，发送信息
                for (String id : ids) {
                    ChannelWriteAndFlushUtil.writeAndFlush(id, users, messageEntity, MsgActionEnum.CHAT.type);
                }
            }

        } else if (type == MsgActionEnum.NOTICEREQUEST.type) {
            // 申请添加好友，通知对方
            To to = dataContent.getTo();
            ChannelWriteAndFlushUtil.writeAndFlush(to.getId(), users, null, MsgActionEnum.NOTICEREQUEST.type);
        } else if (type == MsgActionEnum.NEWGROUP.type) {
            // 通知好友，刷新群列表
            To to = dataContent.getTo();
            ChannelWriteAndFlushUtil.writeAndFlush(to.getId(), users, null, MsgActionEnum.NEWGROUP.type);
        } else if (type == MsgActionEnum.NEWFRIEND.type) {
            // 通知好友，更新好友列表
            Mine mine = dataContent.getMine();
            To to = dataContent.getTo();
            UserMessage userMessage = new UserMessage(to.getType(), to.getAvatar(),
                    to.getUsername(),
                    Long.valueOf(to.getGroupid()),
                    Long.valueOf(to.getId()), to.getSign());
            System.out.println(userMessage.toString());
            ChannelWriteAndFlushUtil.writeAndFlush(mine.getId(), users, userMessage, MsgActionEnum.NEWFRIEND.type);
        } else if (type == MsgActionEnum.ONLINE.type) {
            // 通知我的在线好友，我在线了
            Mine mine = dataContent.getMine();
            userService = SpringUtil.getBean(UserService.class);
            List<User> userList = userService.selectMyOnlineFriend(mine.getId());
            if (null != userList)
                userList.forEach(user ->
                        ChannelWriteAndFlushUtil.writeAndFlush(user.getId().toString(), users, mine, MsgActionEnum.ONLINE.type));

        } else if (type == MsgActionEnum.OFFLINE.type) {
            Mine mine = dataContent.getMine();
            // 通知我的在线好友，我已离线
            userService = SpringUtil.getBean(UserService.class);
            List<User> userList = userService.selectMyOnlineFriend(mine.getId());
            userList.forEach(user ->
                    ChannelWriteAndFlushUtil.writeAndFlush(user.getId().toString(), users, mine, MsgActionEnum.OFFLINE.type));
        } else if (type == MsgActionEnum.ADMIN_CONNECT.type) {
            String id = dataContent.getMine().getId();
            UserChannelRel.put("Admin:" + id, currentChannel);
        }else if(type == MsgActionEnum.REJECTUSER.type){
            String id = dataContent.getTo().getId();
            ChannelWriteAndFlushUtil.writeAndFlush(id, users, null, MsgActionEnum.REJECTUSER.type);
            UserChannelRel.put("Forbidden:"+id,UserChannelRel.remove(id));
        } else if (type == MsgActionEnum.KEEPALIVE.type) {
            //  心跳类型的消息
            System.out.println("收到来自[Channel:" + currentChannel.id() + "]的心跳包");
        }
    }

    /**
     * 当客户端连接服务端之后（打开连接）
     * 获取客户端的channel，并且放到channelGroup中去进行管理
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        users.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        String id = UserChannelRel.getKeyByValue(ctx.channel());
        if (null == id){
            return;
        }
        // 当触发handlerRemoved，ChannelGroup会自动移除对应客户端的channel
        ChatHandler.users.remove(ctx.channel());
        if (id.contains("Admin:")) {
            System.out.println("移除管理员channel: " + ctx.channel().id());
        }else if(id.contains("Forbidden:")){
            System.out.println("禁用channel: " + ctx.channel().id());
            // 设置状态为Forbidden
            userService = SpringUtil.getBean(UserService.class);
            id = id.substring(id.indexOf(":") + 1);
            User mine = userService.selectUserById(Long.valueOf(id));
            userService.updateUserStatusToForbidden(id);
            // 通知我的在线好友，我已离线
            List<User> userList = userService.selectMyOnlineFriend(id);
            userList.forEach(user ->
                    ChannelWriteAndFlushUtil.writeAndFlush(user.getId().toString(), users, mine, MsgActionEnum.OFFLINE.type));
        } else {
            lock.lock();
            try {
                System.out.println("移除channel: " + ctx.channel().id());
                // 设置用户为离线状态
                userService = SpringUtil.getBean(UserService.class);
                // 查询我的信息
                User mine = userService.selectUserById(Long.valueOf(id));
                // 更改我的在线状态
                userService.updateUserTrueStatusToOffline(id);
                userService.updateUserStatusToOffline(id);
                // 通知我的在线好友，我已离线
                List<User> userList = userService.selectMyOnlineFriend(id);
                userList.forEach(user ->
                        ChannelWriteAndFlushUtil.writeAndFlush(user.getId().toString(), users, mine, MsgActionEnum.OFFLINE.type));

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 发生异常之后关闭连接（关闭channel），随后从ChannelGroup移除
        ctx.channel().close();
        users.remove(ctx.channel());
    }
}
