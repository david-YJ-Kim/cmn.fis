package com.absolics.solace.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPStreamingPublishCorrelatingEventHandler;

public class PubEventHandler implements JCSMPStreamingPublishCorrelatingEventHandler {
	private static final Logger log = LoggerFactory.getLogger(PubEventHandler.class);
	
	@Override
	public void handleErrorEx(Object messageID, JCSMPException cause, long timestamp) {
		log.error("Producer received error for msg.");
	}

	@Override
	public void responseReceivedEx(Object messageID) {
		log.info("Producer received response for msg.");
		
	}
}
