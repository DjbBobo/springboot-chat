package com.bo.redis;

import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
public abstract class RedisBasePrefixKey implements RedisPrefixKey{

    /**
     * 过期时间
     */
    private Long expireSeconds;

    /**
     * redis key 前缀
     */
    private String prefix;

    /**
     * 构造器
     * expireSeconds    为零默认为永不过期
     * @param prefix    前缀
     */
    public RedisBasePrefixKey(String prefix){
        this.prefix = prefix;
        this.expireSeconds = 0L;
    }

    /**
     * 获取过期时间
     * @return
     */
    @Override
    public Long getExpireSeconds() {
        return expireSeconds;
    }

    /**
     * 获取Key前缀
     * @return
     */
    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className.concat(":").concat(prefix).concat(":");
    }
}
