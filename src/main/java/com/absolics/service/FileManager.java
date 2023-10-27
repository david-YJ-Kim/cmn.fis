package com.absolics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.absolics.config.SFTPProperty;

@Service
public class FileManager {

	@Autowired
	private SFTPProperty ftpProp;
	
	public boolean moveToFile(String filePath, String fileName) {
		
		
		
		return false;
	}
	
}
