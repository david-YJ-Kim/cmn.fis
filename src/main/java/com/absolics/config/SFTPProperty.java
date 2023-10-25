package com.absolics.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;;

@Component
public class SFTPProperty {
	private static final SFTPProperty isntance = new SFTPProperty();

	@Value("$nas.sftp.host}")
	private String host;
	
	@Value("$nas.sftp.port}")
	private int port;
	
	@Value("$nas.sftp.user-name}")
	private String userName;
	
	@Value("$nas.sftp.user-passwd}")
	private String userPasswd;

	@Value("$nas.sftp.remote-target-dir}")
	private String remoteTargetDir;
	
	private SFTPProperty() {}
	
	public static SFTPProperty getSftpProperty() {
		return isntance;
	}
	
	public Session getSftpChannel() throws JSchException {
		ChannelSftp ch ;
		JSch jsch = new JSch();
		Session session = jsch.getSession(getUserName(), getHost(), getPort());
		session.setPassword(getUserPasswd());
		session.setConfig("StrictHostKeyChecking", "no");
		
		return session;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserPasswd() {
		return userPasswd;
	}

	public String getRemoteTargetDir() {
		return remoteTargetDir;
	}	
}
