package com.absolics.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.solacesystems.jcsmp.JCSMPChannelProperties;
import com.solacesystems.jcsmp.JCSMPProperties;

@Component
public class SolaceConfiguration {
	private static SolaceConfiguration instance;
	Environment env;
	
	@Value("${solace.java.host")
	private String host;
	
	@Value("${solace.java.msg-vpn}")
    private String msgVpn;
	
	@Value("${solace.java.client-username}")
    private String clientUserName;
	
	@Value("${solace.java.client-password}")
    private String clientPassWord;
	
	@Value("${solace.java.client-name}")
    private String clientName;
	
	@Value("${solace.java.reconnnect-retries}")
    private int reconnnectRetries;
	
	@Value("${solace.java.retries-per-host}")
    private int retriesPerHost;
		
	@Value("${solace.java.module-name}")
    private String moduleName;
	
	public SolaceConfiguration(Environment env) {
		this.env = env;
		instance = this;
	}
	
	public static SolaceConfiguration getSessionConfiguration() {
		return instance;
	}
	
	private Map<String, String> argBag = new HashMap<String, String>();
	
	public JCSMPProperties getProperty(String postfixClientName){
		JCSMPProperties properties = new JCSMPProperties();

		properties.setProperty(JCSMPProperties.HOST, host);
		//solace msgVpn명
		properties.setProperty(JCSMPProperties.VPN_NAME, this.msgVpn);
		//solace msgVpn에 접속할 클라이언트사용자명
		properties.setProperty(JCSMPProperties.USERNAME, this.clientUserName);
		//solace msgVpn에 접속할 클라이언트사용자 패스워드(생략 가능)
		if(this.clientPassWord != null && !this.clientPassWord.isEmpty())
			properties.setProperty(JCSMPProperties.PASSWORD, this.clientPassWord);
		//Allication client name 설정 - 동일 msgVpn 내에서 uniq 해야 함
		properties.setProperty(JCSMPProperties.CLIENT_NAME, this.clientName + postfixClientName);
		//endpoint에 등록되어 있는 subscription으로 인해 발생하는 에러 무시
		properties.setProperty(JCSMPProperties.IGNORE_DUPLICATE_SUBSCRIPTION_ERROR, true);

		JCSMPChannelProperties chProp = new JCSMPChannelProperties();

		chProp.setReconnectRetries(getReconnnectRetries()); // 세션 다운 시 재 연결 트라이 횟수
		chProp.setConnectRetriesPerHost(getRetriesPerHost()); // 세션 리트라이 간격
		properties.setProperty(JCSMPChannelProperties.RECONNECT_RETRIES, chProp);

		return properties;
	}

	public String getHost() {
		return env.getProperty(host);
	}

	public String getMsgVpn() {
		return msgVpn;
	}

	public String getClientUserName() {
		return clientUserName;
	}

	public String getClientPassWord() {
		return clientPassWord;
	}

	public String getClientName() {
		return clientName;
	}

	public int getReconnnectRetries() {
		return reconnnectRetries;
	}

	public int getRetriesPerHost() {
		return retriesPerHost;
	}

	public String getModuleName() {
		return moduleName;
	}

	public Map<String, String> getArgBag() {
		return argBag;
	}
	
}
