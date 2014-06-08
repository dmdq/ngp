/*
 * Unicode码转换工具类
 * 
 * @FileName UnicodeUtil.java
 * @author  xiaofeng.ma
 */
package com.ngnsoft.ngp.util;

import org.springframework.util.StringUtils;

/**
 * 
 * @author Administrator
 */
public class UnicodeUtil {
	
	public static String toHexString(Integer i){
		StringBuffer s = new StringBuffer(Integer.toHexString(i));
		while (s.length() < 4) {
			s.insert(0, "0");
		}
		return s.toString();
	}

	public static String getUnicode(String s) {
		if (StringUtils.hasText(s)) {
			StringBuffer ret = new StringBuffer();
			StringBuffer nn = null;
			for (int i = 0; i < s.length(); i++) {
				nn = new StringBuffer(Integer.toHexString(s.charAt(i)));
				// 不够4位前面补0
				while (nn.length() < 4) {
					nn.insert(0, "0");
				}

				ret.append("\\u").append(nn);
			}

			return ret.toString();
		} else {
			return s;
		}
	}

	public static String fromUnicode(String s) {
		if (StringUtils.hasText(s)) {
			String[] s2 = s.split("\\\\u");
			StringBuffer s1 = new StringBuffer();
			for (int i = 1; i < s2.length; i++) {
				s1.append((char) Integer.parseInt(s2[i], 16));
			}

			return s1.toString();
		} else {
			return s;
		}
	}
	
	public static String getUnicodeWithoutU(String s){
		if (StringUtils.hasText(s)) {
			StringBuffer ret = new StringBuffer();
			StringBuffer nn = null;
			for (int i = 0; i < s.length(); i++) {
				nn = new StringBuffer(Integer.toHexString(s.charAt(i)));
				// 不够4位前面补0
				while (nn.length() < 4) {
					nn.insert(0, "0");
				}

				ret.append(nn);
			}

			return ret.toString();
		} else {
			return s;
		}
	}
	

	public static String fromUnicodeWithoutU(String s) {
		if (StringUtils.hasText(s)) {
			StringBuffer s1 = new StringBuffer();
			for (int i = 0; i < (s.length()/4); i++) {
				s1.append((char) Integer.parseInt(s.substring(i*4, (i+1)*4), 16));
			}

			return s1.toString();
		} else {
			return s;
		}
	}
	
	public static String appendUnicodePrefix(String unicode) {
		if(unicode == null || unicode.trim().equals("")) {
			return "8000";
		}
		int length = unicode.length()/2;
		String lengthHex = Integer.toHexString(length);
		if(lengthHex.length() < 2) {
			lengthHex = "0" + lengthHex;
		}
		return "80" + lengthHex + unicode;
	}
}
