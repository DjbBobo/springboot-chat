package com.bo.redis;

public interface RedisPrefixKey {
    /**
     * redis 过期时间
     * @return
     */
    Long getExpireSeconds();

    /**
     * redis key 前缀
     * @return
     */
    String getPrefix();
}
