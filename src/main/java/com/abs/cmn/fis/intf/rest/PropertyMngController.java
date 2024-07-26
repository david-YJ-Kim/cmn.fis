package com.abs.cmn.fis.intf.rest;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.abs.cmn.fis.config.FisSharedInstance;
import com.abs.cmn.fis.intf.solace.InterfaceSolacePub;
import com.abs.cmn.fis.message.vo.receive.FisFileReportVo;
import com.abs.cmn.fis.util.FisMessageList;
import com.solacesystems.jcsmp.JCSMPException;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping(value = "/fis/management")
public class PropertyMngController {



    @RequestMapping(value = "/send/FIS_FILE_REPORT", method = RequestMethod.POST)
    public String testMethod(@RequestBody FisFileReportVo fisFileReportVo) throws JCSMPException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper().configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        log.info(fisFileReportVo.toString());
        InterfaceSolacePub.getInstance().sendBasicTextMessage(FisMessageList.FIS_FILE_REPORT, mapper.writeValueAsString(fisFileReportVo), FisSharedInstance.getInstance().getReceiveTopicName());
        return null;
    }
}
