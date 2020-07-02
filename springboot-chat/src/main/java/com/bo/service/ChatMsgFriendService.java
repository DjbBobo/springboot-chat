package com.bo.service;

import com.bo.mapper.ChatMsgFriendMapper;
import com.bo.netty.MessageEntity;
import com.bo.pojo.ChatMsgFriend;
import com.bo.vo.ChatLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ChatMsgFriendService {

    @Autowired
    private ChatMsgFriendMapper chatMsgFriendMapper;

    public Integer insertMsg(ChatMsgFriend chatMsgFriend){
        return chatMsgFriendMapper.insertMsg(chatMsgFriend);
    }

    public Integer signMsg(String from, String to) {
        return chatMsgFriendMapper.signMsg(from,to);
    }

    public List<MessageEntity> selectUnSignMsg(String to) {
        return chatMsgFriendMapper.selectUnSignMsg(to);
    }

    public List<ChatLogVo> getFriendChatLog(String uid, String fid) {
        return chatMsgFriendMapper.getFriendChatLog(uid,fid);
    }
}
