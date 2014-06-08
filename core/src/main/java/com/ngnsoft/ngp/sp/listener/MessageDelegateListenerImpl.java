package com.ngnsoft.ngp.sp.listener;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.ngnsoft.ngp.sp.MessageDelegateListener;

public class MessageDelegateListenerImpl extends MessageDelegateListener {
	
	private static Logger logger = Logger.getLogger(MessageDelegateListenerImpl.class);

	/**
	 * accept and handle message by channel 
	 */
	@Override
	protected void handleMessage(String channel, Serializable message) {
		logger.info("The message had been Accepted:>>> " + message);
	}

}
