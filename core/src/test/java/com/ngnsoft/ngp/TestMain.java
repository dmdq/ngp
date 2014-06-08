package com.ngnsoft.ngp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import com.jcraft.jzlib.ZInputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.FileCopyUtils;

public class TestMain {
	
	private static String host = "http://42.120.9.103:8080/ngs/";
	
//	private static String host = "http://localhost:8080/ngp-server/";

    public static void main(String[] args) throws Exception {
//    	Request gr = getCocReq("APRQ", "c423e389-42f7-4e20-99bb-5bb99a24b71e");
//    	System.out.println("req:"+gr.getData());
//    	send(gr);
//    	String name = "男2.png";
//    	String uuid = Integer.toHexString(name.hashCode());
//    	System.out.print(Integer.MAX_VALUE^1606);
    }
    
    public static void send(Request req) {
        try {
        	 String url = host + "gr";
             
             HttpClient httpClient = new DefaultHttpClient();
             HttpPost httpPost = new HttpPost(url);  
             
             List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
             nameValuePairs.add(new BasicNameValuePair("rq", req.getData().toString()));
             httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
             // Execute HTTP Post Request
             HttpResponse response = httpClient.execute(httpPost);
       
             HttpEntity responseEntity = response.getEntity();  
       
             System.out.println("----------------------------------------");  
             System.out.println(response.getStatusLine());  
             
             
             ZInputStream zin = new ZInputStream(responseEntity.getContent());
             BufferedReader reader = new BufferedReader(new InputStreamReader(zin));
             String line;
             StringBuffer content = new StringBuffer();
             while ((line = reader.readLine()) != null) {
                 content.append(line + "\n");
             }
             reader.close();
             String result = content.toString();
             System.out.println(result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    
    private static Request getReq(String key, String sid) throws JSONException {
        Request jo = getBaseReq();
        jo.put("key", key);
        if (sid != null) {
            jo.put("sid", sid);
        }
        return jo;
    }
    
    private static Request getBaseReq() throws JSONException {
        Request jo = new Request();
//        jo.setAppId("553777703");
//        jo.setAppId("com.toktoo.robinhood");
        jo.setAppId("111111");
        jo.setAppVersion("1.2.0");
        jo.setMacId("fcy1");
//        jo.setDeviceId("698924496740b8edc5f7870c99b4866f");
        return jo;
    }
    
    private static Request getCocReq(String key, String sid) throws Exception {
        Request jo = getReq(key, sid);
        String jsonData = FileCopyUtils.copyToString(new FileReader("e:/json.txt"));
        JSONObject biz = new JSONObject(jsonData);
        
        
//        JSONObject upd = new JSONObject();
//        
//        List<String> list = new ArrayList<String>();
//        list.add("a");
//        list.add("b.x");
//        list.add("c.v.m");
//        JSONArray del = new JSONArray(list);
//        JSONObject map = new JSONObject();
//        upd.put("coin", 100L);
//        upd.put("trop", 1000);
//        map.put("canon", "canon-json-string");
//        map.put("xbow", "xbow-json-string");
//        upd.put("map", map);
//        biz.put("upd", upd);
//        biz.put("del", del);
        jo.put("biz", biz);
        return jo;
    }

}
