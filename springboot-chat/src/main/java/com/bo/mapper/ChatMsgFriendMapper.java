package com.bo.mapper;

import com.bo.netty.MessageEntity;
import com.bo.pojo.ChatMsgFriend;
import com.bo.vo.ChatLogVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ChatMsgFriendMapper {

    Integer insertMsg(ChatMsgFriend chatMsgFriend);

    Integer signMsg(@Param("from") String from, @Param("to") String to);

    List<MessageEntity> selectUnSignMsg(@Param("to") String to);

    List<ChatLogVo> getFriendChatLog(@Param("from") String uid, @Param("to") String fid);
}
