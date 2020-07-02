package com.bo.mapper;

import com.bo.pojo.FriendRequest;
import com.bo.pojo.GroupRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GroupRequestMapper {

    Integer selectUnReadRequestByGroupRequest(GroupRequest groupRequest);

    Integer insertGroupRequest(GroupRequest groupRequest);

    Integer getUnReadGroupRequestNum(@Param("uid") Long id);

    List<FriendRequest> getGroupRequest(@Param("id") String id);

    void setGroupRequestRead(@Param("id") String id);
}
