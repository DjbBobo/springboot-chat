package com.bo.service;

import com.bo.config.QQConfigInfo;
import com.bo.mapper.QQUserMapper;
import com.bo.pojo.QQUser;
import com.bo.pojo.User;
import com.bo.utils.HttpClientUtils;
import com.bo.utils.IdWorker;
import com.bo.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class QQUserService {
 
    @Autowired
    private QQUserMapper qqUserMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private UserService userService;
    @Autowired
    private UserFriendGroupService userFriendGroupService;
 
    /**
     * 保存qq账户
     * @param code
     * @return
     * @throws Exception
     */
    public User saveQQAccount(String code) throws Exception{
        // 获取access token
        String access_url = QQConfigInfo.GETACCESSTOKEN+"?grant_type=authorization_code" +
                "&client_id=" + QQConfigInfo.APPID +
                "&client_secret=" + QQConfigInfo.APPKEY +
                "&code=" + code +
                "&redirect_uri=" + QQConfigInfo.BACKURL;
        String access_res = HttpClientUtils.doGet(access_url);
        String access_token = "";
        if (access_res.indexOf("access_token") >= 0) {
            String[] array = access_res.split("&");
            for (String str: array)
                if (str.indexOf("access_token") >= 0) {
                    access_token = str.substring(str.indexOf("=") + 1);
                    break;
                }
        }

        // 获取qq账户 openId
        String open_id_url = QQConfigInfo.GETACCOUNTOPENID+"?access_token="+access_token;
        String open_id_res = HttpClientUtils.doGet(open_id_url);
        int startIndex = open_id_res.indexOf("(");
        int endIndex = open_id_res.lastIndexOf(")");
        String open_id_res_str = open_id_res.substring(startIndex + 1, endIndex);
        HashMap map = JsonUtils.jsonToPojo(open_id_res_str, HashMap.class);
        String openid = (String) map.get("openid");

        // 获取账户qq信息
        String account_info_url = QQConfigInfo.GETACCOUNTINFO+"?access_token="+access_token+
                "&oauth_consumer_key=" + QQConfigInfo.APPID +
                "&openid=" + openid;
        String account_info_res = HttpClientUtils.doGet(account_info_url);
        Map<String,Object> userMap = JsonUtils.jsonToPojo(account_info_res, HashMap.class);
        User user = new User();
        QQUser qqOpen = new QQUser();
        qqOpen.setOpenId(openid);
        userMap.forEach((k,v)->{
            if (k.equals("nickname"))
                user.setUsername((String) v);
            if (k.equals("figureurl_2"))
                user.setAvatar((String) v);
        });
        // 判断openid在系统中是否存在
        User isExits = qqUserMapper.getQQUserByOpenId(openid);
        if(isExits == null){
            // 插入
            Long account = Long.valueOf(String.valueOf(idWorker.nextId()).substring(10));
            String id = String.valueOf(idWorker.nextId()).substring(11);
            user.setAccount(String.valueOf(account));
            user.setId(id);
            userService.addQQUser(user);
            qqUserMapper.addQQUser(account, openid);
            userFriendGroupService.addUserFriendGroup(id,"好友");
        }else {
            // 更新
            String account = qqUserMapper.selectQQUserAccountByOpenId(openid);
            user.setAccount(account);
            userService.updateQQUser(user);
        }
        return user;
    }
}