package com.bo.service;

import com.bo.mapper.ChatMsgGroupMapper;
import com.bo.vo.ChatLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ChatMsgGroupService {

    @Autowired
    private ChatMsgGroupMapper chatMsgGroupMapper;

    public Integer insertMsg(String id, String groupId, String from, String content, long time) {
        return chatMsgGroupMapper.insertMsg(id,groupId,from,content,time);
    }

    public List<ChatLogVo> getGroupChatLog(String groupId) {
        return chatMsgGroupMapper.getGroupChatLog(groupId);
    }
}
