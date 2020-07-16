package com.zhanghui.poem.Cache;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheException;
import redis.clients.jedis.Jedis;

import java.io.Serializable;
import java.util.concurrent.locks.ReadWriteLock;

public class RedisCacheUtil implements Cache {
    private final String id;
    private Jedis cache = new Jedis("192.168.32.157", 6379);

    public RedisCacheUtil(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    /**
     * @return 返回的是当前缓存中key的总数
     * 9223372036854775807
     * 2147483647
     */
    @Override
    public int getSize() {
        Long dbSize = cache.dbSize();
        return dbSize.intValue();
    }

    /**
     * 向缓存中存入数据
     *
     * @param key
     * @param value
     */
    @Override
    public void putObject(Object key, Object value) {
        //对参数的对象进行序列化操作
        byte[] keyBytes = SerializationUtils.serialize((Serializable) key);
        byte[] valueBytes = SerializationUtils.serialize((Serializable) value);
        cache.set(keyBytes, valueBytes);
    }

    /**
     * 从缓存中取出数据
     *
     * @param key
     * @return
     */
    @Override
    public Object getObject(Object key) {
        System.out.println("缓存的key:" + key);
        byte[] keyBytes = SerializationUtils.serialize((Serializable) key);
        // 从redis中取出缓存的数据
        byte[] valueBytes = cache.get(keyBytes);
        Object o = null;
        if (valueBytes != null) {
            // 将查询返回的字节数组，反序列化成java对象
            o = SerializationUtils.deserialize(valueBytes);
        }
        return o;
    }

    /**
     * 移除缓存中指定的数据
     *
     * @param key
     * @return
     */
    @Override
    public Object removeObject(Object key) {
        byte[] keyBytes = SerializationUtils.serialize((Serializable) key);

        // 从redis中取出缓存的数据
        byte[] valueBytes = cache.get(keyBytes);
        Object o = null;
        if (valueBytes != null) {
            // 将查询返回的字节数组，反序列化成java对象
            o = SerializationUtils.deserialize(valueBytes);
        }
        // 移除指定key对应的数据
        Long del = cache.del(keyBytes);

        return o;
    }

    @Override
    public void clear() {
        cache.flushDB();
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (getId() == null) {
            throw new CacheException("Cache instances require an ID.");
        }
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cache)) {
            return false;
        }

        Cache otherCache = (Cache) o;
        return getId().equals(otherCache.getId());
    }

    @Override
    public int hashCode() {
        if (getId() == null) {
            throw new CacheException("Cache instances require an ID.");
        }
        return getId().hashCode();
    }


}
