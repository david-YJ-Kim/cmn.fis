package com.absolics.config;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import lombok.Getter;;

@Getter
@Component
public class SFTPConfiguration {
	private static final Logger log = LoggerFactory.getLogger(SFTPConfiguration.class);
	
	private static final SFTPConfiguration isntance = new SFTPConfiguration();

	@Value("${nas.sftp.host}")
	private String host;
	
	@Value("${nas.sftp.port}")
	private int port;
	
	@Value("${nas.sftp.user-name}")
	private String userName;
	
	@Value("${nas.sftp.user-passwd}")
	private String userPasswd;

	@Value("${nas.sftp.remote-target-dir}")
	private String remoteTargetDir;
	
	private Session session;
	
	private ChannelSftp channel;
	
	private Properties config;
	
	private JSch jsch;
	
	private SFTPConfiguration() {
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
	
	public static SFTPConfiguration getSftpProperty() {
		return isntance;
	}
	
	
	// 전체 shutdown 할 때 
	private void shutdown() {
		this.channel.disconnect();
		log.info("## Disconnect to Channel.");
		this.session.disconnect();
		log.info("## Disconnect to Session.");
	}
}
