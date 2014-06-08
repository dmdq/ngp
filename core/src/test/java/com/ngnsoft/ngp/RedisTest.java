package com.ngnsoft.ngp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ngnsoft.ngp.sp.redis.RedisImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/spring/applicationContext-*.xml", "classpath:/spring/applicationContext.xml"})
public class RedisTest extends AbstractJUnit4SpringContextTests {
	
	@Autowired
	private RedisImpl redisImpl;

	@Test
	public void setOrSendMessageTest() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", 1);
		map.put("name", "望湘园");
		redisImpl.set("12312", (Serializable)map);
		try {
			/*Set<String> sets = redisImpl.keys(".*");
			for(String c : sets) {
				System.out.println(redisImpl.get(c) + "---------------------------");
			}*/
			redisImpl.clear("*");
			System.out.println(redisImpl.get("12312"));
			//redisImpl.sendMessage(count);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*@Test
	public void getObjectTest() {
		Object s = redisImpl.get("sey");
		System.out.println(s.getClass());
	}*/
	
	
}
