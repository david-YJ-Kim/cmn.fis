package com.abs.cmn.fisnew.util.service;

//import com.abs.cmn.fis.config.SFTPProperty;
import com.abs.cmn.fisnew.config.FisPropertyObject;
import com.abs.cmn.fisnew.util.code.FisConstant;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
@Slf4j
public class FileManager {

	private Boolean localMode = null;
//		// TODO remove filePath+fileName
//		try {
//
//			SFTPProperty.getSftpProperty().getChannel().put(new FileInputStream(file), filePath+fileName);
//			return true;
//
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			log.error("## FileNotFoundException : ", e);
//		} catch (SftpException e) {
//			// TODO Auto-generated catch block
//			log.error("## SftpException : ", e);
//		}
//
//		return false;
//	}


	public Boolean isLocalMode(){
		if(localMode == null){
			localMode = FisPropertyObject.getInstance().getFileMode().equals(FisConstant.local.name()) ? true : false;
		}
		return localMode;
	}

	public File getFile(String path, String fileName) throws Exception {

		if(this.isLocalMode()){
			return this.getFileFromLocal(path, fileName);
		}else{
			return this.getFileFromRemote(path,fileName);
		}

	}

	public boolean deleteFile(String path, String fileName){
		if(this.isLocalMode()){

		}else{

		}
		return false;
	}


	public boolean moveFile(String fromPath, String fileName, String toPath){
		if(this.isLocalMode()){
			if(this.moveLocalFile(fromPath, fileName, toPath)){
				return true;
			}
		}else{

		}
		return false;
	}

	private boolean moveLocalFile(String fromPath, String fileName, String toPath){
		File fromFile = new File(fromPath, fileName);
		File toFile = new File(toPath, fileName);

		if(!fromFile.exists()){
			log.error("Source file does not exist.");
			return false;
		}

		if(!toFile.getParentFile().exists()){
			boolean created = toFile.getParentFile().mkdir();
			if(!created){
				log.error("Failed to create destination directory.");
				return false;
			}
		}

		Path sourcePath = fromFile.toPath();
		Path destinationPath = toFile.toPath();
		try {
			Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}


	private File getFileFromLocal(String path, String name) throws Exception {
		File file = new File(path, name);
		if(file.exists()){
			log.info("File Exist: {}", file.getAbsolutePath() );
			return file;
		}else {
			log.error("File Not exist");
			throw new Exception(String.format("File is not exist under path: %s ", (path + name)));
		}
	}

	private File getFileFromRemote(String path, String name) throws Exception {
		try {
			// TODO : FileStream 을파일로 변환해서 return 한다.
//			SFTPProperty.getSftpProperty().getChannel().get(path+fileName);

			return null;

		} catch (Exception e) {
			e.printStackTrace();
			log.error("## SftpException : ", e);
			log.error("File Not exist");
			throw new Exception(String.format("File is not exist under path: %s ", (path + name)));
		}
	}
	
//	public boolean deleteFile(String path, String fileName) {
//		try {
//
//			SFTPProperty.getSftpProperty().getChannel().rm(path + "/" + fileName);
//			return true;
//
//		} catch (SftpException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return false;
//	}
	
//	public static void main(String args[]) {
//		String path = "/home/snis";
//		String fileName = "Absolics 계측 결과 파일 표준_20230918_test.xlsx";
////		File localFile;
//
//		// FTP 서버 정보
//        String host = SFTPProperty.getSftpProperty().getHost();
//        int port = SFTPProperty.getSftpProperty().getPort();
//        String username = "mestest";	//"snis";
//        String password = "absadmin";	//"!sn12910@";
//
//        String localPath = "D:\\test.xlsx";
//
//        JSch jsch = new JSch();
//        Session session = null;
//        try {
//
//            session = jsch.getSession(username, host, port);
//            session.setConfig("StrictHostKeyChecking", "no");
//            session.setPassword(password);
//            session.connect();
//
//            ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
//            channel.connect();
//
//            try {
//				channel.get(path + "/" + fileName, new FileOutputStream(localPath));
//				log.info("download!! ");
//				channel.rm(path + "/" + fileName);
//				log.info("removed !! ");
//
//	            channel.put(localPath, path + "/" + "123"+fileName);
//
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//            channel.disconnect();
//            session.disconnect();
//        } catch (JSchException | SftpException e) {
//            e.printStackTrace();
//        }
//
//	}
}
