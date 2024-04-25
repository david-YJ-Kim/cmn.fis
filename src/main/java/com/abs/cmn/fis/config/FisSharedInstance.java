package com.abs.cmn.fis.config;

import com.abs.cmn.fis.domain.rule.mng.FisRuleManager;
import com.abs.cmn.fis.intf.solace.InterfaceSolacePub;
import com.abs.cmn.fis.intf.solace.InterfaceSolaceSub;
import com.abs.cmn.fis.util.FisCommonUtil;
import com.abs.cmn.fis.util.vo.ParseRuleRelVo;
import com.abs.cmn.fis.util.vo.ParseRuleVo;
import com.abs.cmn.seq.SequenceManager;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Component
public class FisSharedInstance {


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
    @Value("${ap.interface.destination.receive.topic}")
    private String receiveTopicName;
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
    @Value("${ap.query.getDelList}")
    private String getDeleteListSql;
    @Value("${ap.query.deleteCnWork}")
    private String deleteCnWork;
    @Value("${ap.query.insertWorkHistory}")
    private String insertWorkHistory;

    @Value("${ap.query.rule.parsing}")
    private String selectParsingRuleDataSql;

    @Value("${ap.query.rule.mapping}")
    private String selectMappingRuleDataSql;

    @Value("${ap.shutdown.force.timeout.ms}")
    private int apShutdownForceTimeoutMs;

    @Value("${ap.shutdown.polling.interval.ms}")
    private int apShutdownPollingIntervalMs;

    @Value("${ap.worker.pool-size.core}")
    private int corePoolSize;  // 기본 실행 대기하는 Thread 수

    @Value("${ap.worker.pool-size.max}")
    private int maxPoolSize;  // 동시 동작하는 최대 Thread 수

    @Value("${ap.worker.capacity}")
    private int queueCapacity;  // MaxPoolSize 초과 요청 시, 최대 Queue 저장 수

    @Value("${ap.worker.name.prefix}")
    private String threadPrefixName; // 생성되는 Thread 접두사 명

    @Value("${ap.seq.rule.path}")
    private String seqRulePath; // 시퀀스 룰 파일 경로

    @Value("${ap.seq.rule.name}")
    private String seqRuleName; // 시퀀스 룰 파일 이름

    @Value("${ap.file.type.default}")
    private String fileTypeDefault;

    private String dateFormatPattern = "yyyy-MM-dd HH:mm:ss.SSS";

    private SimpleDateFormat dateFormatter;


    // 패치 예정인 룰정보
    private List<ParseRuleVo> prepParsingRule;
    private List<ParseRuleRelVo> prepMappingRule;

    // 직전 사용 하던 룰 정보 - 롤백을 대비한 보관
    private List<ParseRuleVo> pastParsingRule;
    private List<ParseRuleRelVo> pastMappingRule;

    private Map<String, ParseRuleVo> ruleVoMap;
    private Map<String, ParseRuleVo> nextRuleVoMap;
    private Map<String, ParseRuleVo> prevRuleVoMap;

    private InterfaceSolaceSub interfaceSolaceSub;

    private InterfaceSolacePub interfaceSolacePub;

    private FisRuleManager fisRuleManager;

    private SequenceManager sequenceManager;


    private static FisSharedInstance instance;

    // Public method to get the Singleton instance
    public static FisSharedInstance createInstance(Environment env) {
        if (instance == null) {
            synchronized (FisSharedInstance.class) {
                // Double-check to ensure only one instance is created
                if (instance == null) {
                    instance = new FisSharedInstance(env);
                }
            }
        }

        if(instance.clientName == null){
            instance.clientName = FisCommonUtil.generateClientName(instance.groupName, instance.siteName, instance.envType, instance.processSeq);
        }

        if(instance.ruleVoMap == null){
            instance.ruleVoMap = new ConcurrentHashMap<>();
        }

        if(instance.sequenceManager == null){
            try {
                instance.sequenceManager = new SequenceManager(instance.groupName, instance.siteName, instance.envType,
                        instance.seqRulePath, instance.seqRuleName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        if(instance.dateFormatter == null){
            instance.dateFormatter = new SimpleDateFormat(instance.getDateFormatPattern());

        }

        return instance;
    }

    public static FisSharedInstance getInstance(){
        return instance;
    }
    public FisSharedInstance(Environment env) {
        this.env = env;
        instance = this;
    }



    public void setDateFormatter(SimpleDateFormat dateFormatter) {
        this.dateFormatter = dateFormatter;
    }

    public void setInterfaceSolaceSub(InterfaceSolaceSub interfaceSolaceSub) {
        this.interfaceSolaceSub = interfaceSolaceSub;
    }

    public void setInterfaceSolacePub(InterfaceSolacePub interfaceSolacePub) {
        this.interfaceSolacePub = interfaceSolacePub;
    }

    public void setSequenceManager(SequenceManager sequenceManager) {
        this.sequenceManager = sequenceManager;
    }

    public Map<String, ParseRuleVo> getRuleVoMap() {
        if(ruleVoMap == null){
            ruleVoMap = new ConcurrentHashMap<>();
        }
        return ruleVoMap;
    }

    public Map<String, ParseRuleVo> getNextRuleVoMap() {
        if(nextRuleVoMap == null){
            nextRuleVoMap = new ConcurrentHashMap<>();
        }
        return nextRuleVoMap;
    }

    public void setRuleVoMap(Map<String, ParseRuleVo> ruleVoMap) {
        this.ruleVoMap = ruleVoMap;
    }

    public void setNextRuleVoMap(Map<String, ParseRuleVo> nextRuleVoMap) {
        this.nextRuleVoMap = nextRuleVoMap;
    }

    public void setPrevRuleVoMap(Map<String, ParseRuleVo> prevRuleVoMap) {
        this.prevRuleVoMap = prevRuleVoMap;
    }

}
