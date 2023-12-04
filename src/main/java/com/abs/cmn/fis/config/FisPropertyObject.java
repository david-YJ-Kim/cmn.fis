package com.abs.cmn.fis.config;

import com.abs.cmn.fis.intf.solace.InterfaceSolacePub;
import com.abs.cmn.fis.intf.solace.InterfaceSolaceSub;
import com.abs.cmn.fis.util.FisCommonUtil;
import com.abs.cmn.fis.util.vo.ParseRuleRelVo;
import com.abs.cmn.fis.util.vo.ParseRuleVo;
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
    @Value("${ap.info.sequence}")
    private String processSeq;
    
    private String clientName;
    
    @Value("${ap.interface.destination.receive.queue}")
    private String receiveQueueName;
    @Value("${ap.interface.destination.receive.init}")
    private String receiveInitTopic;
    @Value("${ap.interface.destination.send.topic}")
    private String sendTopicName;
    
    @Value("${ap.query.batchSize}")
    private Integer batchSize;
    @Value("${ap.query.table-name.insp}")
    private String tableNameInsp;
    @Value("${ap.query.table-name.meas}")
    private String tableNameMeas;
    
    @Value("${ap.query.insert-template}")
    private String insertBatchTemplate;    
    @Value("${ap.query.del-template}")
    private String deleteBatchTemplate;
    
    @Value("${ap.query.rule.parsing}")
    private String selectParsingRuleDataSql;

    @Value("${ap.query.rule.mapping}")
    private String selectMappingRuleDataSql;


    @Value("${ap.shutdown.force.timeout.ms}")
    private int apShutdownForceTimeoutMs;

    @Value("${ap.shutdown.polling.interval.ms}")
    private int apShutdownPollingIntervalMs;
    
    // 프로세스에서 사용하는 룰 객체 
    private List<ParseRuleVo> parsingRule;
    private List<ParseRuleRelVo> mappingRule;
    
    // 패치 예정인 룰정보
    private List<ParseRuleVo> prepParsingRule;
    private List<ParseRuleRelVo> prepMappingRule;
    
    // 직전 사용 하던 룰 정보 - 롤백을 대비한 보관
    private List<ParseRuleVo> pastParsingRule;
    private List<ParseRuleRelVo> pastMappingRule;

    private InterfaceSolaceSub interfaceSolaceSub;

    private InterfaceSolacePub interfaceSolacePub;
    

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

    public void setParsingRule(List<ParseRuleVo> parsingRule) {
        this.parsingRule = parsingRule;
    }

    public void setMappingRule(List<ParseRuleRelVo> mappingRule) {
        this.mappingRule = mappingRule;
    }
    
    public void setPrepParsingRule(List<ParseRuleVo> parsingRule) {
        this.prepParsingRule = parsingRule;
    }

    public void setPrepMappingRule(List<ParseRuleRelVo> mappingRule) {
        this.prepMappingRule = mappingRule;
    }
    
    public void setPastParsingRule(List<ParseRuleVo> parsingRule) {
        this.pastParsingRule = parsingRule;
    }

    public void setPastMappingRule(List<ParseRuleRelVo> mappingRule) {
        this.pastMappingRule = mappingRule;
    }

    public void setInterfaceSolaceSub(InterfaceSolaceSub interfaceSolaceSub) {
        this.interfaceSolaceSub = interfaceSolaceSub;
    }

    public void setInterfaceSolacePub(InterfaceSolacePub interfaceSolacePub) {
        this.interfaceSolacePub = interfaceSolacePub;
    }
}
