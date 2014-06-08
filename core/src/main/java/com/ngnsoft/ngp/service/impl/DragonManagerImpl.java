package com.ngnsoft.ngp.service.impl;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.service.DragonManager;
import com.ngnsoft.ngp.service.UserSessionManager;
import com.ngnsoft.ngp.sp.redis.RedisImpl;

/**
 *
 * @author fcy
 */
@Service
public class DragonManagerImpl extends GenericManagerImpl implements DragonManager {

	@Autowired
	private UserSessionManager usm;

	@Autowired
	@Qualifier("redisImpl")
	private RedisImpl redis;

	@Override
	public Response executeAppMessageByAction(Request req) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}
}
