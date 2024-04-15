//package com.abs.cmn.fis.message;
//
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Test;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Service;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//@Slf4j
//@ActiveProfiles("local")
//public class FisFileReportTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    @DisplayName("Test File Parsing request")
//    public void wfsLoadReq_NormalTest() throws Exception {
//
//        String eqpId = "AP-YG-09-01";
//        String fileType = "INSP";
//        String filePath = "C:\\workspace\\abs\\cmn.fis\\src\\main\\resources\\file\\";
//        String fileName = "parsing_sample.csv";
//
//
//        String uriFormat = "/fis/send/FIS_FILE_REPORT?eqpId=%s&fileType=%s&filePath=%s&fileName=%s";
//
//        mockMvc.perform(MockMvcRequestBuilders.get(String.format(uriFormat, eqpId, fileType, filePath, fileName))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print());
//    }
//}
