package com.absolics.config;

import java.io.File;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;;

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
	
	private Session session;
	
	private Channel chennel ;
	
	private SFTPProperty() {}
	
	public static SFTPProperty getSftpProperty() {
		return isntance;
	}
	
	private ChannelSftp getSftpChannel() throws JSchException {		

		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		
		JSch jsch = new JSch();
		this.session = jsch.getSession(getUserName(), getHost(), getPort());
		session.setPassword(getUserPasswd());
		session.setConfig(config);
		session.connect();
		
		this.chennel = (ChannelSftp) session.openChannel("sftp");
		chennel.connect();
		
		return (ChannelSftp)this.chennel;
	}
	
	public File getFile(String path, String fileName) {
		
		File file = new File(path+fileName);
		
		try {		
			getSftpChannel().get(path+fileName, file.getAbsolutePath());
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return file;
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
