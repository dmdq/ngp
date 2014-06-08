package com.ngnsoft.ngp.protocol.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import com.ngnsoft.ngp.Request;

public class USLGTest extends HttpBaseTest {

	@Override
	@Test
	public void testProtocol() throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
//		paramMap.put("idfa", "1EE90CAD-DED6-4A61-AB8B-C09AB91B2F0C");
		Request req = getReq(paramMap);
		req.put("sid", "1234");
    	System.out.println("req:" + req.getData());
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("rq", req.getData().toString()));
    	postSend(host, req, nameValuePairs);
	}

	@Override
	protected String getProtocolName() {
		return "USLG";
	}

	
	@Override
	protected Request getBaseReq() {
		Request req = new Request();
		req.setAppId("100005");
		req.setAppVersion("11"); 
		req.setMacId("de5c8997f6fb7f285885947b5ef0e883");
		return req;
	}

	@Override
	protected JSONObject getBizData() {
		JSONObject biz = null;
		try {
			biz = new JSONObject();
//			JSONArray array = new JSONArray();
//			array.put("all");
//			biz.put("appData", array);
//			biz.put("sns", "facebook");
//			biz.put("facebook", "100004094410136");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return biz;
	}
	


}
