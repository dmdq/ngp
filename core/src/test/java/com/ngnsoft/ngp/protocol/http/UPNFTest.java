package com.ngnsoft.ngp.protocol.http;

import java.io.FileReader;
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
import org.springframework.util.FileCopyUtils;

import com.ngnsoft.ngp.Request;


public class UPNFTest extends HttpBaseTest {

	@Override
	@Test
	public void testProtocol() throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("sid", "candymatch");
		paramMap.put("enc", 0);
		Request req = getReq(paramMap);
    	System.out.println("req:" + req.getData());
    	
    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("rq", req.getData().toString()));
    	postSend(host, req, nameValuePairs);
	}

	@Override
	protected String getProtocolName() {
		return "UPNF";
	}

	@Override
	protected Request getBaseReq() {
		Request jo = new Request();
		jo.setAppId("com.toktoo.candymatchCMGE");
		jo.setAppVersion("16");
		jo.setMacId("fc84d43ac9608aee");
		return jo;
	}

	@Override
	protected JSONObject getBizData() {
		JSONObject biz = new JSONObject();
		/*try {
			String jsonData = FileCopyUtils.copyToString(new FileReader("e:/srh.txt"));
			biz = new JSONObject(jsonData);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return biz;
	}
	
	public static void main(String[] args) throws ParseException {
		//1370665095000
		
		//1370704343000
		
		//1370665403000
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(df.parse("2013-07-04 15:44:38").getTime());
		
		System.out.println((1370844777000l - 1370752737864l)/(3600 * 1000));
	}

}
