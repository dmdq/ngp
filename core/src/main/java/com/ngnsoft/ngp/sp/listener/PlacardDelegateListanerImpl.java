package com.ngnsoft.ngp.sp.listener;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.ngnsoft.ngp.sp.MessageDelegateListener;
import com.ngnsoft.ngp.sp.handler.MessageHandler;

public class PlacardDelegateListanerImpl extends MessageDelegateListener {
	
	private Logger LOGGER = LoggerFactory.getLogger(PlacardDelegateListanerImpl.class);
	
	private Map<String, MessageHandler> channelMaps = new HashMap<String, MessageHandler>();

	/**
	 * accept and handle message of the placard module 
	 */
	@Override
	protected void handleMessage(String channel, Serializable message) {
		try {
			LOGGER.debug("Start accpeting message>>>.");
			if(channelMaps.containsKey(channel)) {
				channelMaps.get(channel).handle(message);
			}
		} catch (Exception e) {
			LOGGER.error("Handle " + channel + " message ERROR.", e);
		}
	}

	public void setChannelMaps(Map<String, MessageHandler> channelMaps) {
		this.channelMaps = channelMaps;
	}

}
