package com.abs.cmn.fis.message.vo.receive;

import com.abs.cmn.fis.message.vo.common.FisMsgHead;
import com.abs.cmn.fis.util.FisMessageList;
import com.abs.cmn.fis.util.code.FisFileType;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class FisFileReportVoTest {

    public static void main(String[] args) {

        String eqpId = "EQP_ID";
        String fileType = "INSP";
        String filePath = "PATH";
        String fileName = "NAME";

        String sample = "\n" +
                "{\n" +
                "\t\"head\": {\n" +
                "\t\t\"cid\": \"FIS_FILE_REPORT\",\n" +
                "\t\t\"tid\": \"TRACKTIONID_00000000001\",\n" +
                "\t\t\"osrc\": \"\",\n" +
                "\t\t\"otgt\": \"\",\n" +
                "\t\t\"src\": \"WFS\",\n" +
                "\t\t\"srcEqp\": \"\",\n" +
                "\t\t\"tgt\": \"FIS\",\n" +
                "\t\t\"tgtEqp\": []\n" +
                "\n" +
                "\t},\n" +
                "\t\"body\":{\n" +
                "\t\t\"eqpId\": \"%s\",\n" +
                "\t\t\"fileType\": \"%s\",\n" +
                "\t\t\"filePath\": \"%s\",\n" +
                "\t\t\"fileName\": \"%s\"\n" +
                "\t}\n" +
                "\n" +
                "}";

        String payload = String.format(sample, eqpId, fileType, filePath, fileName);

        System.out.println(
                payload
        );
    }

    public static void main_generateVO(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        FisFileReportVo fisFileReportVo = new FisFileReportVo();

        FisMsgHead head = new FisMsgHead();
        head.setCid(FisMessageList.FIS_FILE_REPORT);
        head.setSrc("WFS");
        head.setTgt("FIS");

        FisFileReportVo.FisFileReportBody body = new FisFileReportVo.FisFileReportBody();
        body.setEqpId("AM-YG-09-01");
        body.setFileType(FisFileType.INSP);
        body.setFilePath("");
        body.setFileName("Name");


        fisFileReportVo.setHead(head);
        fisFileReportVo.setBody(body);

        System.out.println(
                fisFileReportVo.toString()
        );


        String sample = "\n" +
                "{\n" +
                "\t\"head\": {\n" +
                "\t\t\"cid\": \"FIS_FILE_REPORT\",\n" +
                "\t\t\"tid\": \"TRACKTIONID_00000000001\",\n" +
                "\t\t\"osrc\": \"\",\n" +
                "\t\t\"otgt\": \"\",\n" +
                "\t\t\"src\": \"WFS\",\n" +
                "\t\t\"srcEqp\": \"\",\n" +
                "\t\t\"tgt\": \"FIS\",\n" +
                "\t\t\"tgtEqp\": []\n" +
                "\n" +
                "\t},\n" +
                "\t\"body\":{\n" +
                "\t\t\"eqpId\": \"AM-YG-09-01\",\n" +
                "\t\t\"fileType\": \"INSP\",\n" +
                "\t\t\"filePath\": \"/data/abs/mos_cmn/fis/files/\",\n" +
                "\t\t\"fileName\": \"inspection_file_20231116.csv\"\n" +
                "\t}\n" +
                "\n" +
                "}";

        FisFileReportVo vo = mapper.readValue(sample, FisFileReportVo.class);
        System.out.println(
                vo.getBody().toString()
        );


    }




}
