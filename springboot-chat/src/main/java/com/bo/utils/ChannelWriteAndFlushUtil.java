package com.bo.utils;

import com.bo.netty.DataMessage;
import com.bo.netty.MessageEntity;
import com.bo.netty.UserChannelRel;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class ChannelWriteAndFlushUtil {

    public static boolean writeAndFlush(String id, ChannelGroup users, Object messageEntity,Integer type){
        Channel receiverChannel = UserChannelRel.get(id);
        if (receiverChannel == null){
            // 用户离线，推送消息（JPush，个推，小米推送）
            System.out.println("用户ID "+id+" 离线");
            return false;
        }else{
            Channel findChannel = users.find(receiverChannel.id());
            if (findChannel != null){
                // 在线
                DataMessage message = new DataMessage(type, messageEntity);
                receiverChannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.objectToJson(message)));
                return true;
            }else{
                // 离线消息推送
                System.out.println("用户ID "+id+" 离线");
                return false;
            }
        }
    }
}
