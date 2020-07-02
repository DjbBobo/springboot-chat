package com.bo.mapper;

import com.bo.pojo.UserFriendGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserFriendGroupMapper {
    List<UserFriendGroup> selectUserFriendGroupsById(String uid);

    Integer addUserFriendGroup(@Param("id") String id, @Param("groupname") String groupname);
}
