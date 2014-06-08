package com.ngnsoft.ngp.protocol.http;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.junit.Test;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.util.DateUtil;


public class LGSLTest extends HttpBaseTest {
	

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
		return "LGSL";
	}

	@Override
	protected Request getBaseReq() {
		Request jo = new Request();
		jo.setAppId("test");
		jo.setAppVersion("10");
		jo.setMacId("628fea95df01df2a9cd2b639d28cc0a3");
		return jo;
	}

	@Override
	protected JSONObject getBizData() {
		JSONObject biz = new JSONObject();
		try {
			biz.put("pid", "fire");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return biz;
	}
	
	public static void main(String[] args) {
		System.out.println(DateUtil.getWeekNum(new Date()));
	}
	 
	/*public static void main(String[] args) throws Exception {
		APRQTest test = new APRQTest();
		for(int i= 0; i < 1000; i++) {
			test.testProtocol();
		}
	}*/

}
