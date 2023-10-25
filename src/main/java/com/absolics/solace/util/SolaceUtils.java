package com.absolics.solace.util;

import java.util.Map;
import java.util.Map.Entry;

import com.absolics.config.SolaceConfiguration;
import com.solacesystems.jcsmp.InvalidPropertiesException;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.JCSMPProperties;
import com.solacesystems.jcsmp.JCSMPSession;
import com.solacesystems.jcsmp.SessionEventHandler;

public class SolaceUtils {
	public static final String xmldoc = "<sample>1</sample>";
	public static final String xmldocmeta = "<sample><metadata>1</metadata></sample>";
	public static final String attachmentText = "my attached data";
	
	/**
	 * Creates a new JCSMPSession. If session creation fails, this static method will exit the process.
	 * 
	 * @param sc (mandatory) a java bean representing session configuration
	 * @param (optional) evtHdlr a session event handler callback 
	 * @param extra (optional) extra session properties not covered in sc
	 * @return a new JCSMPSession instance
	 */
	public static JCSMPSession newSession(SolaceConfiguration sc, SessionEventHandler evtHdlr, Map<String, Object> extra) {
		JCSMPProperties properties = new JCSMPProperties();

		properties.setProperty(JCSMPProperties.HOST, sc.getHost());
		properties.setProperty(JCSMPProperties.VPN_NAME, sc.getMsgVpn());
		properties.setProperty(JCSMPProperties.USERNAME, sc.getClientUserName());
		properties.setProperty(JCSMPProperties.PASSWORD, sc.getClientPassWord());
        properties.setProperty(JCSMPProperties.MESSAGE_ACK_MODE, JCSMPProperties.SUPPORTED_MESSAGE_ACK_AUTO);
        
        // Disable certificate checking
        properties.setBooleanProperty(JCSMPProperties.SSL_VALIDATE_CERTIFICATE, false);
        properties.setProperty(JCSMPProperties.AUTHENTICATION_SCHEME, JCSMPProperties.AUTHENTICATION_SCHEME_BASIC);

        
		/*
		 * Allow extra properties to supplement / override the above, when set
		 * by a particular sample.
		 */
		if (extra != null) {
			for (Entry<String, Object> extraProp : extra.entrySet()) {
				properties.setProperty(extraProp.getKey(), extraProp.getValue());
			}
		}

		JCSMPSession session = null;
		try {
			session = JCSMPFactory.onlyInstance().createSession(properties, null, evtHdlr);
			return session;
		} catch (InvalidPropertiesException ipe) {			
			System.err.println("Error during session creation: ");
			ipe.printStackTrace();
			System.exit(-1);
			return null;
		}
	}

}
