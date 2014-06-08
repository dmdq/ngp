package com.ngnsoft.ngp.protocol.http;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.junit.Test;

import com.ngnsoft.ngp.Request;


public class APMSTest extends HttpBaseTest {

	@Override
	@Test
	public void testProtocol() throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Request req = getReq(paramMap);
		paramMap.put("sid", "62ab0d7a-20ce-42f8-88df-dfe81b87e3c3");
		paramMap.put("enc", 1);
		System.out.println("req:" + req.getData());
    	
    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("rq", req.getData().toString()));
    	postSend(host, req, nameValuePairs);
	}

	@Override
	protected String getProtocolName() {
		return "APMS";
	}

	@Override
	protected Request getBaseReq() {
		Request jo = new Request();
		jo.setAppId("android-dragon");
		jo.setAppVersion("1.0.0");
		jo.setMacId("62849ed0519727dbe3a7b95677e2b105");
		jo.setSession("aaa");
		 
		return jo;
	}

	@Override
	protected JSONObject getBizData() {
		JSONObject biz = new JSONObject();
		try {
			JSONObject jo = new JSONObject();
			//get
//			jo.put("type", 1);
////		jo.put("msgId", 10);
////		jo.put("userData", 1);
//			String arrays[] = new String[]{"MainSoldier"};
//			jo.put("appData", arrays);
//			jo.put("perSize", 20);
//			biz.put("get", jo);
			
			//send
//			jo.put("to", "1000036");
			jo.put("title", "weqw");
			jo.put("body", "asda");
			jo.put("type", 0);
			biz.put("send", jo);
			
			// read
//			jo.put("msgid", "3");
//			biz.put("read", jo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return biz;
	}
	
	public static void main(String[] args) throws ParseException {
		//1370665095000
		
		//1370704343000
		
		//1370665403000
		SimpleDateFormat df = new SimpleDateFormat("yyyy MMM dd HH:mm:ss",Locale.US);
		
		System.out.println(df.format(new Date()));
		
//		System.out.println(df.parse("2013-06-09 13:47:19").getTime());
//		
//		System.out.println((1370844777000l - 1370752737864l)/(3600 * 1000));
	}
}
