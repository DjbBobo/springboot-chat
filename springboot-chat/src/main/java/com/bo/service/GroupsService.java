package com.bo.service;

import com.bo.exception.CMSException;
import com.bo.mapper.GroupsMapper;
import com.bo.pojo.Groups;
import com.bo.pojo.User;
import com.bo.result.ResultCodeEnum;
import com.bo.utils.FastDFSClient;
import com.bo.utils.FileUtils;
import com.bo.utils.IdWorker;
import com.bo.utils.JsonUtils;
import com.bo.vo.AllGroupVo;
import com.bo.vo.GroupVo;
import com.bo.vo.GroupVo2;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class GroupsService {

    private static final String filePrefixUrl = "http://39.97.241.159:88/layim/";

    @Autowired
    private GroupsMapper groupsMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private FastDFSClient fastDFSClient;

    public User selectOwnerById(String id) {
        return groupsMapper.selectOwnerById(id);
    }

    public GroupVo2 addGroup(Long ownerId, String avatar, String groupname, String sign) throws Exception {
        // 截取id作为群号
        Long id = Long.valueOf(String.valueOf(idWorker.nextId()).substring(10));
        // 将图片的base64编码转为文件,上传到文件服务器
        String path = "E:\\groupAvatar.png";
//        String path = "/home/chat/groupAvatar.png";
        FileUtils.base64ToFile(path,avatar);
        MultipartFile file = FileUtils.fileToMultipart(path);
        String url = fastDFSClient.uploadBase64(file);
        url = filePrefixUrl+url;
        System.out.println("上传的群头像:"+url);
        Groups groups = new Groups(id, groupname, url, ownerId, sign);
        Integer count = groupsMapper.addGroup(groups);
        if (count > 0)
            return new GroupVo2(id,"group",url,groupname,sign);
        throw new CMSException(ResultCodeEnum.UNKNOWN_ERROR);
    }

    public String searchGroup(String groupid) {
        GroupVo groupVo = groupsMapper.searchGroup(groupid);
        if (null != groupVo){
            List<GroupVo> groupVoList = new ArrayList<>();
            groupVoList.add(groupVo);
            return JsonUtils.objectToJson(groupVoList);
        }
        throw new CMSException(ResultCodeEnum.GROUP_NOT_EXITST);
    }

    public String selectOwnerIdByGroupId(String groupId) {
        return groupsMapper.selectOwnerIdByGroupId(groupId);
    }

    public Groups selectGroup(Long groupId) {
        return groupsMapper.selectGroup(groupId);
    }

    public PageInfo<AllGroupVo> selectAllGroup(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<AllGroupVo> grouList = groupsMapper.selectAllGroup();
        PageInfo<AllGroupVo> pageInfo = new PageInfo<>(grouList);
        return pageInfo;
    }
}
