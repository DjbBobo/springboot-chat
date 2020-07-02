package com.bo.mapper;

import com.bo.pojo.QQUser;
import com.bo.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface QQUserMapper {


    public User getQQUserByOpenId(@Param("openId") String openid);

    void addQQUser(@Param("account") Long account,@Param("openId") String openid);

    String selectQQUserAccountByOpenId(@Param("openId") String openid);
}
