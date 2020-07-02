package com.bo.config;

public class QQConfigInfo {
    /**
     * 域名
     */
    private static final String DOMAIN = "http://127.0.0.1:8080";
 
    /**
     * APPID
     */
    public static final String APPID = "101513767";
 
    /**
     * APPKEY
     */
    public static final String APPKEY = "b1d978cefcf405388893d8e686d307b0";
 
    /**
     * 回调地址
     */
    public static final String BACKURL = DOMAIN+"/QQLogin";
 
    /**
     * 获取qq授权网页
     */
    public static final String GETQQPAGE = "https://graph.qq.com/oauth2.0/authorize";
 
    /**
     * 获取Access Token
     */
    public static final String GETACCESSTOKEN = "https://graph.qq.com/oauth2.0/token";
 
    /**
     * 获取账户OPENID
     */
    public static final String GETACCOUNTOPENID = "https://graph.qq.com/oauth2.0/me";
 
    /**
     * 获取账户信息
     */
    public static final String GETACCOUNTINFO = "https://graph.qq.com/user/get_user_info";
}