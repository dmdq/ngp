package com.ngnsoft.ngp.protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.ngnsoft.ngp.enums.DataType;
import com.ngnsoft.ngp.service.DataManager;
import com.ngnsoft.ngp.service.ZoneManager;

public class PlacardTest extends ProtocolBaseTest {

	@Autowired
	private DataManager dataManager;
	@Autowired
	private ZoneManager zm;
	
	@Override
	public void testProtocol() {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("appId", "553777703");
        queryMap.put("baseId", "553777703");
        queryMap.put("appVersion", "1.0.0");
        
        queryMap.put("rangleList", zm.getZonesByAppAndEngine("553777703", "1", "553777703"));
		List<JSONObject> resultArray = null;
		try {
			resultArray = (List<JSONObject>)dataManager.getDataByType(DataType.PLACARD, queryMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(resultArray);
	}
}
