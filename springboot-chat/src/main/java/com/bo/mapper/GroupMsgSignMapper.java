package com.bo.mapper;

import com.bo.netty.MessageEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GroupMsgSignMapper {
    Integer insertMsg(@Param("id") long id, @Param("groupMsgId") String groupMsgId, @Param("to") String to);

    Integer signMsg(@Param("groupMsgId") String groupMsgId, @Param("to") String to);

    List<MessageEntity> selectUnSignMsg(@Param("to") String to);
}
