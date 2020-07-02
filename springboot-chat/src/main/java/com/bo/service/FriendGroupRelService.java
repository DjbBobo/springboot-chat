package com.bo.service;

import com.bo.mapper.FriendGroupRelMapper;
import com.bo.pojo.FriendGroupRel;
import com.bo.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FriendGroupRelService {

    @Autowired
    private FriendGroupRelMapper friendGroupRelMapper;
    @Autowired
    private IdWorker idWorker;

    public List<FriendGroupRel> selectFriendGroupRelById(String uid) {
        return friendGroupRelMapper.selectFriendGroupRelById(uid);
    }

    public FriendGroupRel selectFriendGroupRelByUidFid(Long uid,Long fid) {
        return friendGroupRelMapper.selectFriendGroupRelByUidFid(uid,fid);
    }

    public Integer insertFriend(String myId, String fromGroup, String uid, String group) {

        Integer c1 = friendGroupRelMapper.insertFriend(myId,group,uid,idWorker.nextId());
        Integer c2 = friendGroupRelMapper.insertFriend(uid,fromGroup,myId,idWorker.nextId());
        if (c1 == c2 && c1 == 1){
            return 1;
        }
        return 0;
    }

    public List<Long> selectFidByUid(String id) {
        return friendGroupRelMapper.selectFidByUid(id);
    }
}
