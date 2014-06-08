package com.ngnsoft.ngp.protocol.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.junit.Test;

import com.ngnsoft.ngp.Request;


public class LSSLTest extends HttpBaseTest  {

	@Override
	@Test
	public void testProtocol() throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Request req = getReq(paramMap);
//		req.put("sid", "6123");
		req.put("enc", 0);
		System.out.println("req:" + req.getData());
    	
    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("rq", req.getData().toString()));
    	postSend(host, req, nameValuePairs);
	}

	@Override
	protected String getProtocolName() {
		return "LSSL";
	}

	@Override
	protected Request getBaseReq() {
		Request jo = new Request();
		jo.setAppId("100002");
		jo.setAppVersion("1.0.0");
		jo.setMacId("abcd1234567");
		jo.setSession("aaa");
		
		return jo;
	}

	@Override
	protected JSONObject getBizData() {
		JSONObject biz = new JSONObject();
		try {
			JSONObject jo = new JSONObject();
			// read
//			jo.put("msgid", "3");
//			biz.put("read", jo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return biz;
	}
	
			
}
		
