package com.ngnsoft.ngp.sp;

import java.io.Serializable;

/**
 *
 * @author fcy
 */
public interface MessageQueue {
    
	public void sendMessage(String channel, Serializable serializable) throws Exception;
    
}
