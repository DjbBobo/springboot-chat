package com.bo.mapper;

import com.bo.pojo.GroupRequest;
import com.bo.pojo.User;
import com.bo.pojo.UserGroup;
import com.bo.vo.GroupVo;
import com.bo.vo.GroupVo2;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserGroupMapper {
    List<GroupVo> selectUserGroupByUid(@Param("uid")String uid);

    Integer addUserGroup(UserGroup userGroup);

    List<String> selectUserIdByGroupId(@Param("groupId") String id);

    int selectMembersById(@Param("id") String id);

    List<User> selectUsersInfoById(@Param("id") String id);

    UserGroup selectUserGroupByUidGroupId(@Param("myId") Long myId,@Param("groupId") Long groupId);

    List<GroupVo2> selectUserGroupVoByUid(@Param("uid") String id);
}
