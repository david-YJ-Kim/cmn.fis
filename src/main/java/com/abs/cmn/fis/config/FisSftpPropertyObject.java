package com.abs.cmn.fis.config;

import com.abs.cmn.fis.util.code.FisConstant;
import com.jcraft.jsch.*;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Getter
@Component
public class FisSftpPropertyObject {
	private static final Logger log = LoggerFactory.getLogger(FisSftpPropertyObject.class);

	Environment env;

	@Value("${ap.file.mode}")
	private String fileMode;

	@Value("${ap.file.nas.sftp.host}")
	private String host;
	
	@Value("${ap.file.nas.sftp.port}")
	private String port;
	
	@Value("${ap.file.nas.sftp.user-name}")
	private String userName;
	
	@Value("${ap.file.nas.sftp.user-passwd}")
	private String userPasswd;

	@Value("${ap.file.nas.sftp.remote-target-dir}")
	private String remoteTargetDir;
	
	@Value("${ap.file.local-path}")
	private String localFilePath;
	
	private Session session;
	
	private Channel channel;
	
	private Properties properties;
	
	private JSch jsch;

	private static FisSftpPropertyObject instance;

	public ChannelSftp getSftpChannel(){

		if(fileMode.equals(FisConstant.remote.name())){

			this.properties = new Properties();
			this.properties.put("StrictHostKeyChecking", "no");

			this.jsch = new JSch();
			try {
				this.session = jsch.getSession(this.userName, this.host, Integer.valueOf(this.port));
				session.setPassword(this.userPasswd);
				session.setConfig(this.properties);
				session.connect();

				this.channel = (ChannelSftp) session.openChannel(FisConstant.sftp.name());
				this.channel.connect();

				
			} catch (JSchException e) {
				// TODO Auto-generated catch block
				log.error("## Error JSchException : ",e);
			}			
		}
		
		return  (ChannelSftp)channel;

	}
	
	public static FisSftpPropertyObject createInstance(Environment env) {

		if (instance == null) {
			synchronized (FisSftpPropertyObject.class) {
				// Double-check to ensure only one instance is created
				if (instance == null) {
					instance = new FisSftpPropertyObject(env);
				}
			}
		}

		log.info(instance.toString());

		return instance;

	}
	
	public static FisSftpPropertyObject getInstance() {
		return instance;
	}

	public FisSftpPropertyObject(Environment env){
		this.env = env;
		instance = this;
	}
	
	
	// 전체 shutdown 할 때
	private void shutdown() {
		this.channel.disconnect();
		log.info("## Disconnect to Channel.");
		this.session.disconnect();
		log.info("## Disconnect to Session.");
	}


	@Override
	public String toString() {
		return "FisSftpPropertyObject{" +
				"env=" + env +
				", host='" + host + '\'' +
				", port=" + port +
				", userName='" + userName + '\'' +
				", userPasswd='" + userPasswd + '\'' +
				", remoteTargetDir='" + remoteTargetDir + '\'' +
				", session=" + session +
				", channel=" + channel +
				", properties=" + properties +
				", jsch=" + jsch +
				'}';
	}
}
