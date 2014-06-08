package com.ngnsoft.ngp.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IOSDesUtil {
	private final static String key = "qq_cmge_key_pp";

	private final static Pattern pattern = Pattern.compile("\\d+");

	private final static String charset="utf-8";

	public static String encode(String src) { 
		try {
			byte[] data = src.getBytes(charset);
			byte[] keys = key.getBytes();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < data.length; i++) {
				int n = (0xff & data[i]) + (0xff & keys[i % keys.length]);
				sb.append("@" + n);
			}
			return sb.toString();
		}catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return src;
	}

	public static String decode(String src) {
		if(src == null || src.length() == 0){
			return src;
		}
		Matcher m = pattern.matcher(src);
		List<Integer> list = new ArrayList<Integer>();
		while (m.find()) {
			try {
				String group = m.group();
				list.add(Integer.valueOf(group));
			} catch (Exception e) {
				e.printStackTrace();
				return src;
			}
		}

		if (list.size() > 0) {
			try {
				byte[] data = new byte[list.size()];
				byte[] keys = key.getBytes();

				for (int i = 0; i < data.length; i++) {
					data[i] = (byte) (list.get(i) - (0xff & keys[i % keys.length]));
				}
				return new String(data, charset);
			} catch (UnsupportedEncodingException e){
				e.printStackTrace();
			}
			return src;
		} else {
			return src;
		}
	}
	
	public static String ntData2SignStr(String ntData) {
    	String ntDataMD5 = MD5Utils.getMD5(ntData);
    	byte[] ntDataBytes = ntDataMD5.getBytes();
    	if(ntDataBytes.length > 13) {
    		byte val1 = ntDataBytes[1];
    		ntDataBytes[1] = ntDataBytes[13];
    		ntDataBytes[13] = val1;
    	}
    	if(ntDataBytes.length > 17) {
    		byte val5 = ntDataBytes[5];
    		ntDataBytes[5] = ntDataBytes[17];
    		ntDataBytes[17] = val5;
		}
    	if(ntDataBytes.length > 23) {
    		byte val7 = ntDataBytes[7];
    		ntDataBytes[7] = ntDataBytes[23];
    		ntDataBytes[23] = val7;
		}
    	return new String(ntDataBytes);
    }
	
}
