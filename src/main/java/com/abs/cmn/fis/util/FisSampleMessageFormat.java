package com.abs.cmn.fis.util;

public class FisSampleMessageFormat {

    public static final String FIS_FILE_REPORT_FORMAT = "\n" +
            "{\n" +
            "\t\"head\": {\n" +
            "\t\t\"cid\": \"%s\",\n" +
            "\t\t\"tid\": \"%s\",\n" +
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

    public static final String FIS_TEST_MESSAGE_FORMAT = "\n" +
            "{\n" +
            "\t\"head\": {\n" +
            "\t\t\"cid\": \"%s\",\n" +
            "\t\t\"tid\": \"%s\",\n" +
            "\t\t\"osrc\": \"\",\n" +
            "\t\t\"otgt\": \"\",\n" +
            "\t\t\"src\": \"TST\",\n" +
            "\t\t\"srcEqp\": \"\",\n" +
            "\t\t\"tgt\": \"FIS\",\n" +
            "\t\t\"tgtEqp\": []\n" +
            "\n" +
            "\t},\n" +
            "\t\"body\":{\n" +
            "\t\t\"sleepTm\": %d\n" +
            "\t}\n" +
            "\n" +
            "}";
}
