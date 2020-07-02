package com.bo.redis;

public class ResultVoKey extends RedisBasePrefixKey {
    private ResultVoKey(Long expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    private ResultVoKey(String prefix) {
        super(prefix);
    }

    public static final String USER_KEY_ID = "id";

    public static ResultVoKey resultVo = new ResultVoKey(3600L,USER_KEY_ID);

}
