package com.ngnsoft.ngp.protocol.http;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.util.FileCopyUtils;

import com.ngnsoft.ngp.Request;


public class APRQTest extends HttpBaseTest {
	
  
	@Override
	@Test
	public void testProtocol() throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>(); 
		/*if(sid == null) {
			paramMap.put("sid", "1");
		} else {
			paramMap.put("sid", sid);
		}*/
		paramMap.put("sid", "5f7d0cfb-1440-47b3-9bc0-2b5f2281a9d2");
		paramMap.put("enc", 0);
		Request req = getReq(paramMap);
    	System.out.println("req:" + req.getData());
    	
    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("rq", req.getData().toString()));
        long startTime = System.currentTimeMillis();
    	postSend(host, req, nameValuePairs);
    	long endTime = System.currentTimeMillis();
    	System.out.println((endTime - startTime));
	}

	@Override
	protected String getProtocolName() {
		return "APRQ";
	}

	@Override
	protected Request getBaseReq() {
		Request jo = new Request();
		jo.setAppId("100005");
		jo.setAppVersion("11");
		jo.setMacId("e04ef7bd2249741532135fc595872898");
		return jo;
	}
 
	@Override
	protected JSONObject getBizData() {
		JSONObject biz = new JSONObject();
		
		try {
			String jsonData = FileCopyUtils.copyToString(new FileReader("e:/htbt.txt"));
			biz = new JSONObject(jsonData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return biz;
	}
	
	public static void main(String[] args) {
		System.out.println(new Date(1390545669142l));
	}
	 
}
