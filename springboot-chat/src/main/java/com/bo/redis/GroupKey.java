package com.bo.redis;

public class GroupKey extends RedisBasePrefixKey{

    public GroupKey(Long expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static final String USER_KEY_ID = "id";

    public static GroupKey groupUid = new GroupKey(3600L, USER_KEY_ID);
}
