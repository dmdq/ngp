package com.ngnsoft.ngp.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.log4j.Logger;

public class RedisUtil {
	
	private static Logger logger = Logger.getLogger(RedisUtil.class);

	public static Object getObjectFromBytes(byte[] objBytes) {
		if (objBytes == null || objBytes.length == 0) {
			return null;
		}
		ByteArrayInputStream bi = new ByteArrayInputStream(objBytes);
		ObjectInputStream oi;
		try {
			oi = new ObjectInputStream(bi);
			return oi.readObject();
		} catch (Exception e) {
			logger.error("Convert byte array to Object failed >>>", e);
		}
		return null;
	}

	public static byte[] getBytesFromObject(Serializable obj) {
		if (obj == null) {
			return null;
		}
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		ObjectOutputStream oo;
		try {
			oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);
		} catch (Exception e) {
			logger.error("Convert object to byte array failed >>>", e);
		}
		return bo.toByteArray();
	}
	
}
