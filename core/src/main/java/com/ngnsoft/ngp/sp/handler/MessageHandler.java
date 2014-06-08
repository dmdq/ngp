package com.ngnsoft.ngp.sp.handler;

import java.io.Serializable;

public interface MessageHandler {
	
	public void handle(Serializable message) throws Exception;

}
