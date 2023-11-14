package com.abs.cmn.fis.config;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;;

@Component
public class SFTPProperty {
	private static final Logger log = LoggerFactory.getLogger(SFTPProperty.class);
	
	private static final SFTPProperty isntance = new SFTPProperty();

	@Value("$nas.sftp.host}")
	private String host="192.168.0.193";
	
	@Value("$nas.sftp.port}")
	private int port=22;
	
	@Value("$nas.sftp.user-name}")
	private String userName="mestest";
	
	@Value("$nas.sftp.user-passwd}")
	private String userPasswd="absadmin";

	@Value("$nas.sftp.remote-target-dir}")
	private String remoteTargetDir="./backup/";
	
	private Session session;
	
	private Channel channel;
	
	private Properties config;
	
	private JSch jsch;
	
	private SFTPProperty() {
		this.config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		
		this.jsch = new JSch();
		try {
			this.session = jsch.getSession(getUserName(), getHost(), getPort());
			session.setPassword(getUserPasswd());
			session.setConfig(config);
			session.connect();
			
			this.channel = (ChannelSftp) session.openChannel("sftp");
			this.channel.connect();
			
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			log.error("## Error JSchException : ",e);
		}		
	}
	
	public static SFTPProperty getSftpProperty() {
		return isntance;
	}
	
	
	// 전체 shutdown 할 때 
	private void shutdown() {
		this.channel.disconnect();
		log.info("## Disconnect to Channel.");
		this.session.disconnect();
		log.info("## Disconnect to Session.");
	}
	
	public String getHost() {
		return this.host;
	}

	public int getPort() {
		return this.port;
	}

	public String getUserName() {
		return this.userName;
	}

	public String getUserPasswd() {
		return this.userPasswd;
	}

	public String getRemoteTargetDir() {
		return this.remoteTargetDir;
	}

	public ChannelSftp getChannel() {
		return (ChannelSftp)this.channel;
	}
	
}
