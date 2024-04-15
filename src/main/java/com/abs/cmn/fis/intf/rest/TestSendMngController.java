package com.abs.cmn.fis.intf.rest;

import com.abs.cmn.fis.message.vo.receive.FisFileReportVo;
import com.abs.cmn.fis.util.FisSampleMessageFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.abs.cmn.fis.config.FisPropertyObject;
import com.abs.cmn.fis.intf.solace.InterfaceSolacePub;
import com.abs.cmn.fis.util.FisMessageList;
import com.solacesystems.jcsmp.JCSMPException;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping(value = "/fis/send")
public class TestSendMngController {




    @RequestMapping(value = "/FIS_FILE_REPORT", method = RequestMethod.GET)
    public String sendFisFileReport(String eqpId, String fileType, String filePath, String fileName) throws JCSMPException {


        log.info("Send Message Print Parameter." +
                        "eqpId: {}, fileType: {}, filePath: {}, fileName: {}",
                eqpId, fileType, filePath, fileName);
        String tid = "FIS_TID_" + System.currentTimeMillis();

        String payload = String.format(FisSampleMessageFormat.FIS_FILE_REPORT_FORMAT, FisMessageList.FIS_FILE_REPORT, tid, eqpId, fileType, filePath, fileName);
        log.info(payload);

        InterfaceSolacePub.getInstance().sendQueueMessage(FisMessageList.FIS_FILE_REPORT, payload, FisPropertyObject.getInstance().getReceiveQueueName());
        return null;
    }

    @RequestMapping(value = "/LOOP/FIS_FILE_REPORT", method = RequestMethod.GET)
    public String sendLoopFisFileReport(String eqpId, String fileType, String filePath, String fileName, int loop) throws JCSMPException {


        for (int i = 0; i < loop; i++) {
            this.sendFisFileReport(eqpId, fileType, filePath, fileName);
        }
        return null;
    }

    @RequestMapping(value = "/FIS_TEST_MESSAGE", method = RequestMethod.GET)
    public String sendFisTestMessage(long sleepTime) throws JCSMPException {


        String tid = "FIS_TID_" + System.currentTimeMillis();

        String payload = String.format(FisSampleMessageFormat.FIS_TEST_MESSAGE_FORMAT, FisMessageList.FIS_TEST_MESSAGE, tid, sleepTime);


        log.debug(payload);
        InterfaceSolacePub.getInstance().sendQueueMessage(FisMessageList.FIS_TEST_MESSAGE, payload, FisPropertyObject.getInstance().getReceiveQueueName());
        return null;
    }

}

