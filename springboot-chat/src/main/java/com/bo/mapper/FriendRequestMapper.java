package com.bo.mapper;

import com.bo.pojo.FriendRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FriendRequestMapper {

    Integer sendFriendRequest(FriendRequest friendRequest);

    List<FriendRequest> getFriendGroupRequest(String id);

    Integer getUnReadRequestNum(Long id);

    void setFriendGroupRequestRead(String id);

    FriendRequest selectRequestByUidFid(@Param("uid") Long uid, @Param("fid") Long fid);

    Integer updateStausAgree(@Param("my_id") String myId, @Param("friend_id") String uid,@Param("time")String time);

    Integer selectUnReadRequestByUidGroupId(@Param("myId") Long myId, @Param("groupId") Long groupId,@Param("type")Integer type);

    Integer sendFriendGroupRequest(FriendRequest friendRequest);

    Integer updateGroupStausAgree(@Param("myId") Long uid, @Param("groupId") Long groupId,@Param("time") String time);

    Integer updateGroupStausRefuse(@Param("uid") String uid, @Param("fromGroup") String fromGroup, @Param("time") String time);

    Integer updateFriendStausRefuse(@Param("myId") String myId, @Param("friendId") String friendId, @Param("time")String time);
}
