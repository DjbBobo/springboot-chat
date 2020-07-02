package com.bo.mapper;

import com.bo.pojo.User;
import com.bo.pojo.UserGroupRel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserGroupRelMapper {
    int selectMembersById(@Param("id") String id);

    List<User> selectUsersInfoById(@Param("id") String id);

    List<String> selectUserIdByGroupId(@Param("mid") String mineId,@Param("id") String id);

    Integer addUserGroupRel(UserGroupRel userGroupRel);
}
