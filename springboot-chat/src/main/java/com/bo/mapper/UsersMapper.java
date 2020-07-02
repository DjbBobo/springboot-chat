package com.bo.mapper;

import com.bo.pojo.User;
import com.bo.pojo.UserFriendGroup;
import com.bo.vo.ReFreshFriendVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UsersMapper{

    User selectUser(User user);

    User selectUserById(@Param("uid") String uid);

    List<User> selectUserByUidGroupId(@Param("uid") String uid, @Param("gid") Long id);

    User searchFriend(String account);

    Integer updateUserStatusToOnline(@Param("id") String mineId);

    Integer updateUserTrueStatusToOnline(@Param("id") String id);

    Integer updateUserStatusToOffline(@Param("id") String id);

    Integer updateUserTrueStatusToOffline(@Param("id") String id);

    List<User> selectMyOnlineFriend(@Param("id") String mineId);

    Integer updateUserSign(@Param("id") String id, @Param("sign") String sign);

    List<User> seleAllUser();

    Integer resetUserPwd(User user);

    Integer forbiddenUser(User user);

    Integer clearForbiddenUser(User user);

    Integer updateUserStatusToForbidden(@Param("id") String id);

    Integer addQQUser(User user);

    Integer updateQQUser(User user);

    User getQQUserByAccount(@Param("account") String account);
}
