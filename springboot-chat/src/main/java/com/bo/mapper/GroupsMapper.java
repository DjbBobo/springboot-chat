package com.bo.mapper;

import com.bo.pojo.Groups;
import com.bo.pojo.User;
import com.bo.vo.AllGroupVo;
import com.bo.vo.GroupVo;
import com.bo.vo.GroupVo2;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GroupsMapper {
    User selectOwnerById(@Param("id") String id);

    Integer addGroup(Groups groups);

    GroupVo searchGroup(@Param("groupid") String groupid);

    String selectOwnerIdByGroupId(@Param("groupId") String groupId);

    Groups selectGroup(@Param("groupId") Long groupId);

    List<AllGroupVo> selectAllGroup();
}
