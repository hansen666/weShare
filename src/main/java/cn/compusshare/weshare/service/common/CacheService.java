package cn.compusshare.weshare.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.JedisPool;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: LZing
 * @Date: 2019/3/6
 * redis缓存service
 */
@Component
public class CacheService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置过期时限
     * @param key
     * @param time
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取剩余时限，秒
     * @param key
     * @return
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    public boolean hasKey(String key){
        return redisTemplate.hasKey(key);
    }

    /**
     * 缓存单个值
     * @param key
     * @param value
     * @return boolean
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 缓存值并设置过期时间
     * @param key
     * @param value
     * @param time 过期时间
     * @param timeUnit 时间单位
     * @return
     */
    public boolean set(String key,Object value,long time,TimeUnit timeUnit){
        try {
            redisTemplate.opsForValue().set(key,value,time,timeUnit);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key获取value
     * @param key
     * @return
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }



    /**
     * 删除一个或多个缓存
     * @param key
     */
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

     //===================List=======================
    /**
     * list缓存单个value
     * @param key
     * @param value
     * @return
     */
    public boolean lSet(String key,Object value){
        try{
            redisTemplate.opsForList().rightPush(key,value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 一次缓存整个List
     * @param key
     * @param value
     * @return
     */
    public boolean lSet(String key, List<Object> value){
        try{
            redisTemplate.opsForList().rightPushAll(key,value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引获取list中的值
     * @param key
     * @param index
     * @return
     */
    public Object lGet(String key,int index){
        try{
            return redisTemplate.opsForList().index(key,index);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list缓存的多个值
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<Object> lGet(String key,int start,int end){
        try{
            return redisTemplate.opsForList().range(key,start,end);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取整个list
     * @param key
     * @return
     */
    public List<Object> lGetAll(String key){
        try{
            return redisTemplate.opsForList().range(key,0,-1);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list长度
     * @param key
     * @return
     */
    public long lGetSize(String key){
        return redisTemplate.opsForList().size(key);
    }

}
