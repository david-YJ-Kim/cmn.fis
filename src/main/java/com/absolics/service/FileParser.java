package com.absolics.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.processing.FilerException;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import com.absolics.mapper.ParsingRuleMapper;

public class FileParser {//extends JpaRepository<String, Object>{

	
	@Autowired
	ParsingRuleMapper parsingDataMapper;
	
	
//	@Bean
//	public ParsingDataStorage dataSource() {
//		
//	}
	
		
	private boolean read(String path, String fileName, int start, int end) throws FileNotFoundException {
		
		List<String> lines = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new FileReader(path+fileName));
		String line;	

		try {
			
			while ( (line = reader.readLine() ) != null ) {
				
				lines.add(line);
				
			}
		} catch (IOException e) {
			// TODO: handle exception
		}
		

		return false;
	}
	
	public boolean insertParsingData(HashMap<String, Object> map) {
		
		return false;
	}
}
