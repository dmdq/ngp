package com.ngnsoft.ngp.sp.handler;

import java.io.Serializable;
import java.util.Map;

import com.ngnsoft.ngp.Engine;

public class PlacardCleanHandler implements MessageHandler {
	
	@Override
	public void handle(Serializable message) throws Exception {
		Map<String, Object> baseIdCacheMap = (Map<String, Object>)Engine.getInstance().getBaseIdCacheMap();
		Map<String, Object> appIdCacheMap = (Map<String, Object>)Engine.getInstance().getAppIdCacheMap();
		baseIdCacheMap.clear();
		appIdCacheMap.clear();
	}

}
