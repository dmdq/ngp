package com.ngnsoft.ngp.protocol.http;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.junit.Test;

import com.ngnsoft.ngp.Request;

public class LSENTest extends HttpBaseTest {

	@Override
	@Test
	public void testProtocol() throws Exception {
		Request req = getReq(null);
    	System.out.println("req:" + req.getData());
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("rq", req.getData().toString()));
    	postSend(host, req, nameValuePairs);
	}

	@Override
	protected String getProtocolName() {
		return "LSEN";
	}

	@Override 
	protected Request getBaseReq() {
		Request req = new Request();
		req.setAppId("100005");
		req.setAppVersion("1.0.0");
		req.setDvid("2840fb7ab2b737cf9cf09ba8d65d00be");
		return req;
	}

	@Override
	protected JSONObject getBizData() {
		return null;
	}

	public static void main(String[] args) {
		System.out.println((1376884094 - 1376868285)/(1000));
	}
	
}