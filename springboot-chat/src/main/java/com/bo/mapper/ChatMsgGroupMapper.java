package com.bo.mapper;

import com.bo.vo.ChatLogVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ChatMsgGroupMapper {
    Integer insertMsg(@Param("id") String id, @Param("groupId") String groupId, @Param("from") String from,
                      @Param("content") String content, @Param("time") long time);

    List<ChatLogVo> getGroupChatLog(@Param("groupId") String groupId);
}
