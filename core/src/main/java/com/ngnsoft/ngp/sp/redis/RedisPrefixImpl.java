package com.ngnsoft.ngp.sp.redis;

import java.io.Serializable;

/**
 *
 * @author fcy
 */
public class RedisPrefixImpl extends RedisImpl {

    private String prefix;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * 根据prefix + key从redis里面取出value
     *
     * @param key key
     * @throws Exception 
     */
    public Serializable get(String key) throws Exception {
        final String key_ = prefix + "_" + key;
        return super.get(key_);
    }

    /**
     * 向redis里面添加(prefix+key)-value格式的数据
     *
     * @param key key
     * @param value value
     * @throws Exception 
     */
    public void set(String key, final Serializable value) throws Exception {
        final String key_ = prefix + "_" + key;
        super.set(key_, value);
    }

    /**
     * 根据prefix+key从redis里面对应记录
     *
     * @param key key
     * @throws Exception 
     */
    public void del(String key) throws Exception {
        final String key_ = prefix + "_" + key;
        super.del(key_);

    }

    /**
     * redis计数
     *
     * @param key key
     * @throws Exception 
     */
    public Long incr(String key) throws Exception {
        final String key_ = prefix + "_" + key;
        return super.incr(key_);
    }

    /**
     * redis计数
     *
     * @param key key
     * @param value value
     * @throws Exception 
     */
    public Long incr(String key, final long value) throws Exception {
        final String key_ = prefix + "_" + key;
        return super.incr(key_, value);
    }

    /**
     * redis计数
     *
     * @param key key
     * @throws Exception 
     */
    public Long decr(String key) throws Exception {
        final String key_ = prefix + "_" + key;
        return super.decr(key_);
    }

    /**
     * redis计数
     *
     * @param key key
     * @param value value
     * @throws Exception 
     */
    public Long decr(String key, final long value) throws Exception {
        final String key_ = prefix + "_" + key;
        return super.decr(key_, value);
    }
    
}
