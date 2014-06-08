package com.ngnsoft.ngp.protocol;

import org.springframework.beans.factory.annotation.Autowired;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.service.GenericManager;
import com.ngnsoft.ngp.sp.redis.RedisImpl;

public class MRGMTest extends ProtocolBaseTest {
	
	private static String PROMO_KEY_PREFIX = "PROMO_";
	
	private static String NULL_PROMO_ID = "0";

	@Autowired
	private GenericManager genericManager;
	@Autowired
	private RedisImpl redisImpl;
	

	@Override
	public void testProtocol() throws Exception {
		Request req = new Request();
		req.setAppId("553777703");
		req.setAppVersion("1.0.0");
		req.setMacId("fcy-test2");
		req.setIP("192.168.0.60");
		req.setKey("MRGM");
		
		
		long uuid = 3;
		long count = redisImpl.incr(PROMO_KEY_PREFIX + "2013-05-21" + "_"
				+ uuid);
		redisImpl.set("key_3_4", "3");
		System.out.println(redisImpl.keys(formatRegex("*" + PROMO_KEY_PREFIX + "2013-05-21" + "*")));
		System.out.println(redisImpl.keys( "*"));
		Response resp = new Response(Response.YES);
	}
	
	private String formatRegex(String pattern) {
		return pattern.replaceAll("\\-", "\\\\-");
	}

}
