package com.ngnsoft.ngp.sp.redis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import com.ngnsoft.ngp.sp.MessageQueue;
import com.ngnsoft.ngp.sp.ShareMemory;
import com.ngnsoft.ngp.util.RedisUtil;

/**
 *
 * @author lmj
 */
public class RedisImpl implements MessageQueue, ShareMemory {
	
	protected static String redisCode = "utf-8";
	
    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;
    
    @Override
    public Long zCountByScore(final Serializable key, final double min, final double max) {
    	return redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.zCount(RedisUtil.getBytesFromObject(key), min, max);
			}
		});
    }
    
    @Override
    public Long zRemRangeByScore(final Serializable key, final double min, final double max) {
    	return redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.zRemRangeByScore(RedisUtil.getBytesFromObject(key), min, max);
			}
		});
    }
    
    @Override
    public byte[] zRangeByIndex(final Serializable key, final long index) {
    	return redisTemplate.execute(new RedisCallback<byte[]>() {
			public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
				Set<byte[]> set= connection.zRange(RedisUtil.getBytesFromObject(key), index, index);
				if(set == null || set.size() == 0) {
					return new byte[]{};
				} else {
					return set.iterator().next();
				}
			}
		});
    }
    
    @Override
    public Set<byte[]> zRange(final Serializable key) {
    	return redisTemplate.execute(new RedisCallback<Set<byte[]>>() {
			public Set<byte[]> doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.zRange(RedisUtil.getBytesFromObject(key), 0, -1);
			}
		});
    }
    
    @Override
    public Set<byte[]> zRangeByScore(final Serializable key, final double min, final double max) {
    	return redisTemplate.execute(new RedisCallback<Set<byte[]>>() {
			public Set<byte[]> doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.zRangeByScore(RedisUtil.getBytesFromObject(key), min, max);
			}
		});
    }
    
    @Override
    public Boolean zadd(final Serializable key, final double score, final Serializable value) {
    	return redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.zAdd(RedisUtil.getBytesFromObject(key), score, RedisUtil.getBytesFromObject(value));
			}
		});
    }
    
    /**
     * push string array to blocking list
     * @param key
     * @param liveTime
     * @param values
     * @throws Exception 
     * @return
     */
    @Override
    public long rpush(final String key, final int liveTime, final String... values) throws Exception {
		return redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				long result = 0;
				for (int i = 0; i < values.length; i++) {
					result = connection.rPush(RedisUtil.getBytesFromObject(key), RedisUtil.getBytesFromObject(values[i]));
				}
				if (liveTime > 0) {
					connection.expire(RedisUtil.getBytesFromObject(key), liveTime);
				}
				return result;
			}
		});
	}
    
    /**
     * get object from blocking list
     * @param key
     * @throws Exception 
     * @return
     */
    @Override
    public List<String> bRPop(final int timeout, final String key) throws Exception {
		return redisTemplate.execute(new RedisCallback<List<String>>() {
			public List<String> doInRedis(RedisConnection connection) throws DataAccessException {
				List<byte[]> results_ = connection.bRPop(timeout, RedisUtil.getBytesFromObject(key));
				List<String> results =  new ArrayList<String>();
				if(results_ != null) {
					for(byte[] bytes : results_) {
						try {
							results.add((String)RedisUtil.getObjectFromBytes(bytes));
						} catch (Exception e) {
							continue;
						}
					}
					return results;
				}
				return null;
			}
		});
	}
    
    @Override
    public void resetBlockingQueue(String key) throws Exception {
    	Long size = redisTemplate.opsForList().size(key);
    	if(size == 1) {
    		return;
    	} else if(size > 1) {
    		
    	}
    }

    /**
     * 根据key从redis里面取出value
     *
     * @param key key
     * @throws Exception 
     */
    @Override
    public Serializable get(final Serializable key) throws Exception {
        return redisTemplate.execute(new RedisCallback<Serializable>() {

            @Override
            public Serializable doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] keyBytes = RedisUtil.getBytesFromObject(key);
                byte[] bytes = connection.get(keyBytes);
                return (Serializable) RedisUtil.getObjectFromBytes(bytes);
            }
        });
    }
    
    /**
     * 根据key从redis技术其中面取出对应key的计数
     *
     * @param key key
     * @throws Exception 
     */
    @Override
    public Long getFromCounter(Serializable key) throws Exception {
    	if(exist(key)) {
    		return incr(key, 0);
    	} else {
    		return new Long(0);
    	}
    }

    /**
     * 向redis里面添加key-value格式的数据
     *
     * @param key key
     * @param value value
     * @throws Exception 
     */
    @Override
    public void set(final Serializable key, final Serializable value) throws Exception {
        redisTemplate.execute(new RedisCallback<Object>() {

            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] key_ = RedisUtil.getBytesFromObject(key);
                byte[] value_ = RedisUtil.getBytesFromObject(value);
				connection.set(key_, value_);
                return true;
            }
        });
    }
    
    /**
     * set key/value if this key is not exist
     */
    @Override
    public boolean setNx(final Serializable key, final int liveTime, final Serializable value) throws Exception {
    	return redisTemplate.execute(new RedisCallback<Boolean>() {

            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
            	byte[] key_ = RedisUtil.getBytesFromObject(key);
                byte[] value_ = RedisUtil.getBytesFromObject(value);
                boolean isExist = connection.setNX(key_, value_);
                if(isExist && liveTime > 0) {
                	connection.expire(key_, liveTime);
                }
                return isExist;
            }
        });
    }
    
    /**
     * 向redis里面添加key-value格式的数据(liveTime单位是second)
     *
     * @param key key
     * @param liveTime liveTime
     * @param value value
     * @throws Exception 
     */
    @Override
    public void set(final Serializable key, final int liveTime, final Serializable value) throws Exception {
    	if(liveTime <= 0) set(key, value);
    	else {
    		redisTemplate.execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    byte[] key_ = RedisUtil.getBytesFromObject(key);
                    byte[] value_ = RedisUtil.getBytesFromObject(value);
                    connection.setEx(key_, liveTime, value_);
                    return true;
                }
            });
    	}
    }

    /**
     * 根据key从redis里面对应记录
     *
     * @param key key
     * @throws Exception 
     */
    @Override
    public void del(final Serializable key) throws Exception {
        redisTemplate.execute(new RedisCallback<Object>() {

            @Override
            public Object doInRedis(RedisConnection connection) {
                byte[] key_ = RedisUtil.getBytesFromObject(key);
                connection.del(key_);
                return null;
            }
        });

    }

    /**
     * redis计数
     *
     * @param key key
     * @throws Exception 
     */
    @Override
    public Long incr(final Serializable key) throws Exception {
        return redisTemplate.execute(new RedisCallback<Long>() {

            @Override
            public Long doInRedis(RedisConnection connection) {
                byte[] key_ = RedisUtil.getBytesFromObject(key);
                return connection.incr(key_);
            }
        });
    }

    /**
     * redis计数
     *
     * @param key key
     * @param value value
     * @throws Exception 
     */
    @Override
    public Long incr(final Serializable key, final long value) throws Exception {
        return redisTemplate.execute(new RedisCallback<Long>() {

            @Override
            public Long doInRedis(RedisConnection connection) {
                byte[] key_ = RedisUtil.getBytesFromObject(key);
                return connection.incrBy(key_, value);
            }
        });
    }

    /**
     * redis计数
     *
     * @param key key
     * @throws Exception 
     */
    @Override
    public Long decr(final Serializable key) throws Exception {
        return redisTemplate.execute(new RedisCallback<Long>() {

            @Override
            public Long doInRedis(RedisConnection connection) {
                byte[] key_ = RedisUtil.getBytesFromObject(key);
                return connection.decr(key_);
            }
        });
    }

    /**
     * redis计数
     *
     * @param key key
     * @param value value
     * @throws Exception 
     */
    @Override
    public Long decr(final Serializable key, final long value) throws Exception {
        return redisTemplate.execute(new RedisCallback<Long>() {

            @Override
            public Long doInRedis(RedisConnection connection) {
                byte[] key_ = RedisUtil.getBytesFromObject(key);
                return connection.decrBy(key_, value);
            }
        });
    }

    /**
     * 通过正则查找key集合
     *
     * @param pattern pattern
     * @throws Exception 
     */
    @Override
    public Set<String> keys(final String pattern) throws Exception {
        return redisTemplate.execute(new RedisCallback<Set<String>>() {
			public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {
				Set<String> results = new HashSet<String>();
				Set<byte[]> datas = connection.keys(pattern.getBytes());
				Iterator<byte[]> iterator = datas.iterator();
				while (iterator.hasNext()) {
					byte[] keys = iterator.next();
					results.add((String)RedisUtil.getObjectFromBytes(keys));
				}
				return results;
			}
		});

    }

    /**
     * 通过正则匹配key删除对应匹配的集合
     *
     * @param pattern pattern
     * @throws Exception 
     */
    @Override
    public void clear(final String pattern) throws Exception {
        Set<String> keys = redisTemplate.keys(pattern);
        if (!keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
    
    /**
     * 删除对应匹配的集合
     *
     * @param keys keys
     * @throws Exception 
     */
    @Override
    public void clear(final Set<String> keys) throws Exception {
        if (!keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 清空redis所有数据
     * @throws Exception 
     */
    @Override
    public void flushDB() throws Exception {
        redisTemplate.execute(new RedisCallback<Object>() {

            @Override
            public Object doInRedis(RedisConnection connection) {
                connection.flushDb();
                return null;
            }
        });
    }
    
    /**
     * Determine whether the key is exist
	 * @throws Exception 
     */
    @Override
	public boolean exist(final Serializable key) throws Exception {
    	return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) {
            	return connection.exists(RedisUtil.getBytesFromObject(key));
            }
        });
	}

    /**
     * 向指定频道发布消息
     *
     * @param channel channel
     * @param serializable serializable
     * @throws Exception 
     */
    @Override
    public void sendMessage(String channel, Serializable serializable) throws Exception {
        redisTemplate.convertAndSend(channel, serializable);
    }
    
    public static void main(String[] args) {
		byte[] b = new byte[]{-84, -19, 0, 5, 115, 114, 0, 34, 99, 111, 109, 46, 110, 103, 110, 115, 111, 102, 116, 46, 110, 103, 112, 46, 109, 111, 100, 101, 108, 46, 71, 108, 111, 98, 97, 108, 79, 98, 106, 101, 99, 116, 96, 29, 29, -65, 78, 0, 56, 99, 2, 0, 0, 120, 112};
		System.out.println(RedisUtil.getObjectFromBytes(b));
	}

}
