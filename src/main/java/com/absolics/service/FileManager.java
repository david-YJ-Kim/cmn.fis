package com.absolics.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.absolics.config.SFTPProperty;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

@Service
public class FileManager {
	private static final Logger log = LoggerFactory.getLogger(FileManager.class);
			
	public boolean moveToFile(String filePath, String fileName, File file) {
		
		// TODO remove filePath+fileName
		try {
			
			SFTPProperty.getSftpProperty().getChannel().put(new FileInputStream(file), filePath+fileName);
			return true;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			log.error("## FileNotFoundException : ", e);
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			log.error("## SftpException : ", e);
		}
		
		return false;
	}
	
	public File getFile(String path, String fileName) {
				
		try {
			// TODO : FileStream 을파일로 변환해서 return 한다.
			SFTPProperty.getSftpProperty().getChannel().get(path+fileName);
			
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			log.error("## SftpException : ", e);
		}
		
		return null;
	}
	
	public boolean deleteFile(String path, String fileName) {
		try {
			
			SFTPProperty.getSftpProperty().getChannel().rm(path + "/" + fileName);
			return true;
			
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static void main(String args[]) {
		String path = "/home/snis";
		String fileName = "Absolics 계측 결과 파일 표준_20230918_test.xlsx";
//		File localFile;
		
		// FTP 서버 정보
        String host = SFTPProperty.getSftpProperty().getHost();
        int port = SFTPProperty.getSftpProperty().getPort();
        String username = "snis";
        String password = "!sn12910@";
        
        String localPath = "D:\\test.xlsx";
        
        
        JSch jsch = new JSch();
        Session session = null;
        try {
        	
            session = jsch.getSession(username, host, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            session.connect();

            ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();

            try {
				channel.get(path + "/" + fileName, new FileOutputStream(localPath));
				log.info("download!! ");
				channel.rm(path + "/" + fileName);
				log.info("removed !! ");

	            channel.put(localPath, path + "/" + "123"+fileName);

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            channel.disconnect();
            session.disconnect();
        } catch (JSchException | SftpException e) {
            e.printStackTrace();
        }
        
	}
}
