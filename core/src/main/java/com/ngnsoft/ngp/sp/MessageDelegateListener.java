package com.ngnsoft.ngp.sp;

import java.io.Serializable;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.ngnsoft.ngp.util.RedisUtil;


public abstract class MessageDelegateListener implements MessageListener {
	
	private RedisSerializer<String> stringSerializer = new StringRedisSerializer();

	protected abstract void handleMessage(String channel, Serializable message);
	
	@Override
	public void onMessage(Message message, byte[] pattern) {
		String channel = (String)stringSerializer.deserialize(message.getChannel());
		Serializable body = (Serializable)RedisUtil.getObjectFromBytes(message.getBody());
		handleMessage(channel, body);
	}
	
}
