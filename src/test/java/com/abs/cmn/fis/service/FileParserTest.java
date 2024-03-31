package com.abs.cmn.fis.service;

import com.abs.cmn.fis.util.FisCommonUtil;
import com.abs.cmn.fis.util.service.FileManager;
import com.abs.cmn.fis.util.service.FileParser;
import com.abs.cmn.fis.util.vo.ExecuteResultVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@ActiveProfiles("local")
public class FileParserTest {

    @Autowired
    FileParser fileParser;

    @Autowired
    FileManager fileManager;

    @Test
    @DisplayName("Test csv file read and load memory.")
    public void readFileAndLoadMemoryTest() throws Exception {

        String trackingKey = "TrackingKeyForTest";
        ExecuteResultVo resultVo = new ExecuteResultVo();

        String fileName = "H_sampleFileSimple.csv";
        String filePath = "C:\\workspace\\abs\\cmn.fis\\src\\main\\resources\\file\\H_sampleFileSimple.csv\\";
        // Generate file for test.
        File file = this.fileManager.getFile(filePath, fileName);

    }
}
