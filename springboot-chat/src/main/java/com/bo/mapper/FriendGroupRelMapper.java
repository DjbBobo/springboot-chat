package com.bo.mapper;

import com.bo.pojo.FriendGroupRel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FriendGroupRelMapper {
    List<FriendGroupRel> selectFriendGroupRelById(@Param("uid") String uid);

    FriendGroupRel selectFriendGroupRelByUidFid(@Param("uid") Long uid,@Param("fid") Long fid);

    Integer insertFriend(@Param("uid") String uid, @Param("from_group") String fromGroup,@Param("fid") String fid,@Param("id") Long id);

    List<Long> selectFidByUid(@Param("uid") String id);
}
