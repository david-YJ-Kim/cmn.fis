package com.abs.cmn.fis.config;

import com.abs.cmn.fis.util.FisCommonUtil;
import com.abs.cmn.fis.util.vo.ParsingRuleVo;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Component
public class FisPropertyObject {


    Environment env;
    @Value("${ap.info.group}")
    private String groupName;
    @Value("${ap.info.site}")
    private String siteName;
    @Value("${ap.info.env}")
    private String envType;

    @Value("${ap.file.mode}")
    private String fileMode;

    @Value("${ap.info.sequence}")
    private String processSeq;
    private String clientName;
    @Value("${ap.interface.destination.receive.queue}")
    private String receiveQueueName;

    @Value("${ap.interface.destination.send.topic}")
    private String sendTopicName;

    @Value("${ap.query.rule.parsing}")
    private String insertParsingInspectionDataSql;

    @Value("${ap.query.rule.mapping}")
    private String insertParsingMeasurementDataSql;

    @Value("${ap.query.rule.rollback}")
    private String rollbackQuery;


    private List<ParsingRuleVo> parsingRule;

    private List<ParsingRuleVo> mappingRule;

    private String[] inspectionColumList;

    private String[] measurementColumList;



    private static FisPropertyObject instance;

    // Public method to get the Singleton instance
    public static FisPropertyObject createInstance(Environment env) {
        if (instance == null) {
            synchronized (FisPropertyObject.class) {
                // Double-check to ensure only one instance is created
                if (instance == null) {
                    instance = new FisPropertyObject(env);
                }
            }
        }

        if(instance.clientName == null){
            instance.clientName = FisCommonUtil.generateClientName(instance.groupName, instance.siteName, instance.envType, instance.processSeq);
        }

        return instance;
    }

    public static FisPropertyObject getInstance(){
        return instance;
    }
    public FisPropertyObject(Environment env) {
        this.env = env;
        instance = this;
    }

    public void setParsingRule(List<ParsingRuleVo> parsingRule) {
        this.parsingRule = parsingRule;
    }

    public void setMappingRule(List<ParsingRuleVo> mappingRule) {
        this.mappingRule = mappingRule;
    }

    public void setInspectionColumList(String[] inspectionColumList) {
        this.inspectionColumList = inspectionColumList;
    }

    public void setMeasurementColumList(String[] measurementColumList) {
        this.measurementColumList = measurementColumList;
    }
}
