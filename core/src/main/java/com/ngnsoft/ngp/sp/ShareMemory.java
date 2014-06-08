package com.ngnsoft.ngp.sp;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 *
 * @author fcy
 */
public interface ShareMemory {
	
	public long rpush(final String key, final int liveTime, final String... values) throws Exception;
	
	public List<String> bRPop(int timeout, final String key) throws Exception;
	
	public void resetBlockingQueue(String key) throws Exception;
    
    public Serializable get(Serializable key) throws Exception;
    
    public Long getFromCounter(Serializable key) throws Exception;
    
    public void set(Serializable key, Serializable value) throws Exception;
    
    public boolean setNx(final Serializable key, final int liveTime, final Serializable value) throws Exception;
    
    public void set(final Serializable key, final int liveTime, final Serializable value) throws Exception;
    
    public void del(Serializable key) throws Exception;
    
    public Long incr(Serializable key) throws Exception;
    
    public Long incr(Serializable key, long value) throws Exception;
    
    public Long decr(Serializable key) throws Exception;
    
    public Long decr(Serializable key, long value) throws Exception;
    
    public Set<String> keys(final String pattern) throws Exception;
    
    public Set<byte[]> zRange(final Serializable key);
    
    public Long zCountByScore(final Serializable key, final double min, final double max);
    
    public byte[] zRangeByIndex(final Serializable key, final long index);
    
    public Long zRemRangeByScore(final Serializable key, final double min, final double max);
    
    public Set<byte[]> zRangeByScore(final Serializable key, final double min, final double max);
    
    public Boolean zadd(final Serializable key, final double score, final Serializable value);
    
    public boolean exist(Serializable key) throws Exception;
    
    public void clear(String pattern) throws Exception;
    
    public void clear(Set<String> keys) throws Exception;
    
    public void flushDB() throws Exception;
    
}
