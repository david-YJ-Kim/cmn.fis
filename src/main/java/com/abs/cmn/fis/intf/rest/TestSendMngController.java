package com.abs.cmn.fis.intf.rest;

import com.abs.cmn.fis.config.FisPropertyObject;
import com.abs.cmn.fis.intf.solace.InterfaceSolacePub;
import com.abs.cmn.fis.message.vo.common.FisMsgHead;
import com.abs.cmn.fis.message.vo.receive.FisFileReportVo;
import com.abs.cmn.fis.util.FisMessageList;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solacesystems.jcsmp.JCSMPException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping(value = "/fis/send")
public class TestSendMngController {


    public static final String payloadFormat = "\n" +
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


    @RequestMapping(value = "/FIS_FILE_REPORT", method = RequestMethod.GET)
    public String testMethod(String eqpId, String fileType, String filePath, String fileName) throws JCSMPException {


        String cid = FisMessageList.FIS_FILE_REPORT;
        String tid = "FIS_TID_" + System.currentTimeMillis();

        String payload = String.format(payloadFormat, cid, tid, eqpId, fileType, filePath, fileName);


        log.info(payload);
        InterfaceSolacePub.getInstance().sendQueueMessage(FisMessageList.FIS_FILE_REPORT, payload, FisPropertyObject.getInstance().getReceiveQueueName());
        return null;
    }
}
