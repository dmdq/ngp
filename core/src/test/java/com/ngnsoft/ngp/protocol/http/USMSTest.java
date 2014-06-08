package com.ngnsoft.ngp.protocol.http;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.junit.Test;

import com.ngnsoft.ngp.Request;


public class USMSTest extends HttpBaseTest {

	@Override
	@Test
	public void testProtocol() throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Request req = getReq(paramMap);
    	System.out.println("req:" + req.getData());
    	
    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("rq", req.getData().toString()));
    	postSend(host, req, nameValuePairs);
	}

	@Override
	protected String getProtocolName() {
		return "USMS";
	}

	@Override
	protected Request getBaseReq() {
		Request jo = new Request();
		jo.setAppId("ios-dragon");
		jo.setAppVersion("1.0.0");
		jo.setMacId("8f355cef9fcf7047b1df0823b17736ad");
		jo.setSession("aaa");
		return jo;
	}

	@Override
	protected JSONObject getBizData() {
		JSONObject biz = new JSONObject();
		try {
			JSONObject jo = new JSONObject();
			//get
			jo.put("curPage", 0);
			jo.put("perSize", 10);
			biz.put("get", jo);
			
			// send
//			jo.put("to", "1000036");
//			jo.put("title", "你好吗？");
//			jo.put("body", "我很好");
//			biz.put("send", jo);
			
			// read
//			jo.put("msgid", "3");
//			biz.put("read", jo);
			
			//del
//			jo.put("msgid", "3");
//			biz.put("del", jo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return biz;
	}
	
	public static void main(String[] args) throws ParseException {
		//1370665095000
		
		//1370704343000
		
		//1370665403000
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(df.parse("2013-06-09 13:47:19").getTime());
		
		System.out.println((1370844777000l - 1370752737864l)/(3600 * 1000));
	}
}
