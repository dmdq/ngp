package com.ngnsoft.ngp.protocol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.model.Pagination;
import com.ngnsoft.ngp.service.UserManager;

public class SearchRiskersTest extends ProtocolBaseTest {
	
	@Autowired
	private UserManager userManager;

	@Override
	@Test
	public void testProtocol() {
		Request req = new Request(); 
		 req.setAppId("553777703");
		 req.setAppVersion("1.0.0");
		 req.setUserId(1000012l);
		 req.setMacId("fcy-test2");
		 req.setIP("192.168.0.60"); 
		 req.setKey("LSEN");
		 JSONObject map = new JSONObject();
		 JSONArray array = new JSONArray();
		 array.put("Map");
		 try {
			map.put("confirm", array);
			map.put("curPage", 0);
			map.put("perSize", 2);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		req.put("biz", map);
		
		Pagination page = new Pagination();
		page.setCurrent_page(0);
		page.setItems_per_page(10);
//			Response res = userManager.searchFacilitator(req);
	}

}
