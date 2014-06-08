package com.ngnsoft.ngp.component.bingo;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.ngnsoft.ngp.component.bingo.model.BingoRobot;
import com.ngnsoft.ngp.component.bingo.service.BingoManager;
import com.ngnsoft.ngp.component.bingo.service.impl.BingoManagerImpl;
import com.ngnsoft.ngp.service.GenericManager;
import com.ngnsoft.ngp.sp.redis.RedisImpl;
import com.ngnsoft.ngp.util.RedisUtil;

@Service
@Lazy(value = false)
public class RobotService {
	
	@Autowired
	@Qualifier("genericManager")
	private GenericManager gm;
	
	@Autowired
	private BingoManager bm;
	
	@Autowired
	private RedisImpl redisImpl;
	
	private Timer timer;
	
	public static final String BINGO_ROBOT_LIST_FREE_REDIS_KEY = "bingo_robot_free_list";
	public static final String BINGO_ROBOT_LIST_ALL_REDIS_KEY = "bingo_robot_all_list";
	
	private static final int DELAY_INTERVAL_TIME = 8000; //millis second
	
	@PostConstruct
	public void initData2Redis() {
		/*try {
			if(!redisImpl.exist(BINGO_ROBOT_LIST_ALL_REDIS_KEY)) {
				List<BingoRobot> robots = gm.findMulti(new BingoRobot());
				for(BingoRobot robot : robots) {
					redisImpl.zadd(BINGO_ROBOT_LIST_ALL_REDIS_KEY, robot.getId(), robot);
					redisImpl.zadd(BINGO_ROBOT_LIST_FREE_REDIS_KEY, robot.getId(), robot);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		timer = new Timer();
		timer.schedule(new PlayTask(), new Date(), DELAY_INTERVAL_TIME);*/
	}
	
	public void initNewRoom(String gid) {
		try {
			if (!redisImpl.exist(BingoManagerImpl.BINGO_START_TIME_REDIS_PREFIX + gid)) {
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public class PlayTask extends TimerTask {

		@Override
		public void run() {
			try {
				Set<byte[]> robotBytes = redisImpl.zRange(BINGO_ROBOT_LIST_ALL_REDIS_KEY);
				for(byte[] robotByte : robotBytes) {
					BingoRobot robot = (BingoRobot)RedisUtil.getObjectFromBytes(robotByte);
					robot.prepareForRun(redisImpl, bm);
					new Thread(robot).start();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
