package com.abs.cmn.fis.util.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import com.abs.cmn.fis.message.vo.receive.FisFileReportVo;
import com.abs.cmn.fis.util.FisCommonUtil;
import com.abs.cmn.fis.util.ToolCodeList;
import org.springframework.stereotype.Service;

import com.abs.cmn.fis.config.FisSftpPropertyObject;
import com.abs.cmn.fis.util.code.FisConstant;
import com.jcraft.jsch.SftpException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileManager {

    private Boolean localMode = null;

    public Boolean isLocalMode(){
        if(localMode == null){
            localMode = FisSftpPropertyObject.getInstance().getFileMode().equals(FisConstant.local.name());
        }
        return localMode;
    }

    /**
     * Access file object at local directory or remote
     * @param trackingKey
     * @param vo
     * @return
     * @throws Exception
     */
    public File getFile(String trackingKey, FisFileReportVo vo) throws Exception {

        log.info("{} Try to achieve file object at local directory : {}", trackingKey, this.localMode);
        String path = vo.getBody().getFilePath();
        String fileName = vo.getBody().getFileName();
        if(this.isLocalMode()){
            try{
                String targetFilePath = this.modifyFilePath(trackingKey, FisSftpPropertyObject.getInstance().getApFileNasPathBase(),
                        vo.getBody().getEqpId(), vo.getBody().getFilePath());
                log.info("{} File path in message is not linux path. convert window path into linux.", trackingKey);
                return this.getFileFromLocal(trackingKey, targetFilePath, fileName);

            }catch (Exception e){

                try{
                    return this.getFileFromLocal(trackingKey, path, fileName);

                }catch (Exception fe){
                    throw fe;
                }
            }

        }else{
            return this.getFileFromRemote(trackingKey, path,fileName);
        }

    }

    public boolean deleteFile(String path, String fileName){
        if(this.isLocalMode()){
            if (this.removeFileLocal(path, fileName))
                return true;
        }else{
            if(this.removeFileFromRemote(fileName, fileName))
                return true;
        }
        return false;
    }

    public boolean copyFile(String fromPath, String fileName, String toPath, String newFileName) {
        if (this.isLocalMode()) {
            return copyLocalFile(fromPath, fileName, toPath, newFileName);
        } else {
            return copyRemoteFile(fromPath, fileName, toPath, newFileName);
        }
    }

    private boolean copyLocalFile(String fromPath, String fileName, String toPath, String newFileName) {
        File fromFile = new File(fromPath, fileName);
        File toFile = new File(toPath, newFileName == null || newFileName.isEmpty() ? fileName : newFileName);

        if (!fromFile.exists()) {
            log.error("Source file does not exist.");
            return false;
        }

        if (!toFile.getParentFile().exists()) {
            boolean created = toFile.getParentFile().mkdirs();
            if (!created) {
                log.error("Failed to create destination directory.");
                return false;
            }
        }

        Path sourcePath = fromFile.toPath();
        Path destinationPath = toFile.toPath();
        try {
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            log.error("Error occurred wile copy file: {}", e.getMessage(), e);
            return false;
        }
    }

    private boolean copyRemoteFile(String fromPath, String fileName, String toPath, String newFileName) {
        File fromFile = null;
        File toFile = new File(toPath, newFileName == null || newFileName.isEmpty() ? fileName : newFileName);

        try {
            fromFile = this.getFileFromRemote("trackingKey", fromPath, fileName);

            if (!fromFile.exists()) {
                log.error("Source file does not exist at remote Server.");
                return false;
            }

            if (this.insertFileToRemote(fromPath, toPath, fileName)) {
                // You may need to modify the logic based on your specific requirements for remote copying
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            log.error("## FileManager, copyRemoteFile : ", e);
            return false;
        }
    }


    public boolean moveFile(String fromPath, String fileName, String toPath){
        if(this.isLocalMode()){
            if(this.moveLocalFile(fromPath, fileName, toPath)){
                return true;
            }
        }else{
            if(this.moveRemoteFile(fromPath, fileName, toPath)){
                return true;
            }
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

    private boolean moveRemoteFile(String fromPath, String fileName, String toPath){
        File fromFile = null;
        File toFile = new File(toPath, fileName);

        try {

            fromFile = this.getFileFromRemote("trackingKey", fromPath, fileName);

            if(!fromFile.exists()){
                log.error("Source file does not exist at remote Server.");
                return false;
            }

            if ( this.insertFileToRemote(fromPath, toPath, fileName) ) {

                this.removeFileFromRemote(fromPath, fileName);
                return true;

            } else {
                return false;
            }

        }catch (Exception e) {
            log.error("## FileManager, moveRemoteFile : ", e);
            return false;
        }

    }


    /**
     * Get file object at local directory
     * @param trackingKey event tracking key
     * @param path target file path
     * @param name target file name
     * @return file object
     */
    private File getFileFromLocal(String trackingKey, String path, String name) throws Exception {

        File file = new File(path, name);
        if(file.exists()){
            log.info("{} File Exist: {}", trackingKey, file.getAbsolutePath() );
            return file;

        }else {
            log.error("{} File Not exist.path: {}  name: {}", trackingKey, path, name);
            throw new NullPointerException(String.format("%s File is not exist under path: %s & name: %s",
                    trackingKey, path, name));

        }
    }

    private boolean removeFileLocal(String path, String name) {
        File localFile = new File(path+name);
        localFile.delete();

        if (localFile.exists())
            return false;
        else
            return true;

    }

    /**
     * Get file object at remote directory (FTP || SFTP)
     * @param trackingKey event tracking key
     * @param path target file path
     * @param name target file name
     * @return file object
     */
    private File getFileFromRemote(String trackingKey,String path, String name) throws Exception {

        String localPath = FisSftpPropertyObject.getInstance().getLocalFilePath();
        File file = null;

        try {
            // TODO : FileStream 을파일로 변환해서 return 한다.

            file = new File (localPath);

            FisSftpPropertyObject.getInstance().getSftpChannel().get(path+name, new FileOutputStream(localPath));

            file = new File(localPath);

            return file;

        } catch (Exception e) {
            e.printStackTrace();
            log.error("## SftpException : ", e);
            log.error("File Not exist");
            throw new Exception(String.format("File is not exist under path: %s ", (path + name)));
        }
    }

    private boolean removeFileFromRemote(String path, String name) {

        try {
            String rmFilePath = path+name;
            FisSftpPropertyObject.getInstance().getSftpChannel().rm(rmFilePath);
            return true;
        } catch (SftpException e) {
            log.error("## FileManager , removeFileFromRemote : ", e);
            return false;
        }

    }

    private boolean insertFileToRemote(String fromPath,String path, String name) {
        try {
            String targetPath = path+name;
            String existFilePath = fromPath+name;
            FisSftpPropertyObject.getInstance().getSftpChannel().put(existFilePath ,targetPath);
            return true;
        } catch (SftpException e) {
            log.error("## FileManager , insertFileToRemote : ", e);
            return false;
        }
    }

    /**
     *
     * @param trackingKey
     * @param basePath
     * @param eqpId
     * @param windowFilePath Y:\\PROD_DEF_ID\\AP-TG-09\\PROC_DEF_ID
     * @return
     */
    public String modifyFilePath(String trackingKey, String basePath, String eqpId, String windowFilePath){

        String linuxDelimiter = "/";

        String linuxPath = FisCommonUtil.convertWindowPathToLinux(windowFilePath);
        switch (eqpId){
            case ToolCodeList.AP_TG_09_01:
            case ToolCodeList.AP_TG_10_01:
            case ToolCodeList.AP_OL_13_01:
            case ToolCodeList.AP_RD_11_01:
                return basePath + FisCommonUtil.detachToolNumber(eqpId) + linuxPath;

            default:
                return basePath + eqpId + linuxDelimiter + linuxPath;
        }


    }
}
