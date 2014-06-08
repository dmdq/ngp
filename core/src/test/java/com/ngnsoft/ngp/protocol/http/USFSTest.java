package com.ngnsoft.ngp.protocol.http;

import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.util.FileCopyUtils;

import com.ngnsoft.ngp.Request;


public class USFSTest extends HttpBaseTest {

	@Override
	@Test
	public void testProtocol() throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(sid == null) {
			paramMap.put("sid", "111111111");
		} else {
//			paramMap.put("sid", sid);
		}
//		paramMap.put("idfa", "1EE95000-DED6-47-AB8B-C09A074B2F30");
		Request req = getReq(paramMap);
    	System.out.println("req:" + req.getData());
    	
    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("rq", req.getData().toString()));
        postSend(host, req, nameValuePairs);
	}

	@Override
	protected String getProtocolName() {
		return "USFS";
	}

	@Override
	protected Request getBaseReq() {
		Request jo = new Request();
		jo.setAppId("100001");
		jo.setAppVersion("1.0");
		jo.setMacId(getUUID());
		return jo;
	}

	@Override
	protected JSONObject getBizData() {
		JSONObject biz = null;
		try {
			String jsonData = FileCopyUtils.copyToString(new FileReader("e:/addFrd.txt"));
			biz = new JSONObject(jsonData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return biz;
	}
	
	private static String getUUID() {
		String s = UUID.randomUUID().toString().replaceAll("_", "");
		return "Test" + s.replaceAll("-", "");
	}
	
}
