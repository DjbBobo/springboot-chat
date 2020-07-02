package com.bo.service;

import com.bo.mapper.GroupMsgSignMapper;
import com.bo.netty.MessageEntity;
import com.bo.pojo.GroupMsgSign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GroupMsgSignService {
    @Autowired
    private GroupMsgSignMapper groupMsgSignMapper;

    public Integer insertMsg(long id, String groupMsgId, String to) {
        return groupMsgSignMapper.insertMsg(id,groupMsgId,to);
    }

    public Integer signMsg(String groupMsgId, String to) {
        return groupMsgSignMapper.signMsg(groupMsgId,to);
    }

    public List<MessageEntity> selectUnSignMsg(String to) {
        return groupMsgSignMapper.selectUnSignMsg(to);
    }
}
