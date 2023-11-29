package com.abs.cmn.fis.intf.rest;

import com.abs.cmn.fis.config.FisPropertyObject;
import com.abs.cmn.fis.intf.solace.InterfaceSolacePub;
import com.abs.cmn.fis.util.FisMessageList;
import com.abs.cmn.fis.util.code.FisFileType;
import com.solacesystems.jcsmp.JCSMPException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping(value = "/fis/management")
public class PropertyMngController {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String testMethod(HttpServletRequest request) throws JCSMPException {

        InterfaceSolacePub.getInstance().sendBasicTextMessage(FisMessageList.FIS_FILE_REQ, "HelloWord", FisPropertyObject.getInstance().getReceiveQueueName(), FisFileType.INSP.name());
        log.info(request.toString());
        return null;
    }
}
