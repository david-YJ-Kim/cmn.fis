package com.abs.cmn.fisnew.activator;

import com.abs.cmn.fisnew.config.FisPropertyObject;
import com.abs.cmn.fisnew.config.SolaceSessionConfiguration;
import com.abs.cmn.fisnew.domain.rule.model.CnFisIfParsingDataMappingInfo;
import com.abs.cmn.fisnew.domain.rule.model.CnFisIfParsingFileInfo;
import com.abs.cmn.fisnew.domain.rule.service.CnFisIfParsingDataMappingInfoService;
import com.abs.cmn.fisnew.domain.rule.service.CnFisIfParsingFileInfoService;
import com.abs.cmn.fisnew.domain.work.service.CnFisWorkService;
import com.abs.cmn.fisnew.domain.work.vo.CnFisWorkSaveRequestVo;
import com.abs.cmn.fisnew.intf.solace.InterfaceSolacePub;
import com.abs.cmn.fisnew.intf.solace.InterfaceSolaceSub;
import com.abs.cmn.fisnew.util.FisCommonUtil;
import com.abs.cmn.fisnew.util.vo.ParsingRuleVo;
import com.solacesystems.jcsmp.JCSMPException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Component
public class FisApStartedActivator implements ApplicationRunner {


    @Autowired
    private Environment env;

    @Autowired
    private CnFisWorkService workService;

    @Autowired
    private CnFisIfParsingDataMappingInfoService cnFisIfParsingDataMappingInfoService;

    @Autowired
    private CnFisIfParsingFileInfoService cnFisIfParsingFileInfoService;

    @Override
    public void run(ApplicationArguments args){
        
        // TODO 기준정보 초기화
        List<CnFisIfParsingDataMappingInfo> mappingInfoEntities = this.cnFisIfParsingDataMappingInfoService.getAllEntities();
        List<ParsingRuleVo> mappingInfoVos = FisCommonUtil.convertMappingInfoInfoVo(mappingInfoEntities);
        FisPropertyObject.getInstance().setMappingRule(mappingInfoVos);

        List<CnFisIfParsingFileInfo> parsingInfoEntities = this.cnFisIfParsingFileInfoService.getAllEntities();
        List<ParsingRuleVo> parsingInfoVos = FisCommonUtil.convertParsingInfoInfoVo(parsingInfoEntities);
        FisPropertyObject.getInstance().setParsingRule(parsingInfoVos);

        log.info("기준정보 로딩 완료. MappingInfos: {}, ParsingInfo: {}",
                FisPropertyObject.getInstance().getMappingRule().toString(), FisPropertyObject.getInstance().getParsingRule().toString());


        // TODO Query 컬럼 리스트
        String[] inspectionColumList = this.parsingColumList(FisPropertyObject.getInstance().getInsertParsingInspectionDataSql());
        String[] measurementColumList = this.parsingColumList(FisPropertyObject.getInstance().getInsertParsingMeasurementDataSql());

        FisPropertyObject.getInstance().setInspectionColumList(inspectionColumList);
        FisPropertyObject.getInstance().setMeasurementColumList(measurementColumList);


        SolaceSessionConfiguration sessionConfiguration = SolaceSessionConfiguration.createSessionConfiguration(env);

        try {
            InterfaceSolacePub interfaceSolacePub = InterfaceSolacePub.getInstance();
            interfaceSolacePub.init();
        } catch (JCSMPException e) {
            throw new RuntimeException(e);
        }

        try {
            InterfaceSolaceSub interfaceSolaceSub = new InterfaceSolaceSub();
            interfaceSolaceSub.run();
        } catch (JCSMPException e) {
            throw new RuntimeException(e);
        }

    }

    private String[] parsingColumList(String query){
        return FisCommonUtil.extractColumns(query);
    }
}
