package com.bo.redis;

public class UserKey extends RedisBasePrefixKey {

    private UserKey(Long expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    private UserKey(String prefix){
        super(prefix);
    }

    public static final String USER_KEY_ID = "id";
    public static final String USER_KEY_TOKEN = "token";

    /**
     * 用户Key
     */
    public static UserKey userId = new UserKey(3600*2L,USER_KEY_ID);
    public static UserKey userToken = new UserKey(3600*2L,USER_KEY_TOKEN);
}
