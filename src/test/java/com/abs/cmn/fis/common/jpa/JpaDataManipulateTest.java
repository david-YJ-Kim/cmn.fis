//package com.abs.cmn.fis.common.jpa;
//
//import com.abs.cmn.fis.domain.rule.model.CnFisParseRuleRel;
//import com.abs.cmn.fis.domain.rule.repository.CnFisParseRuleRelRepository;
//import com.abs.cmn.fis.util.code.FisFileType;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@ActiveProfiles("local")
//@DataJpaTest
//@Slf4j
////@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//// → to prevent Spring Boot from replacing the configured data source with an in-memory database
//public class JpaDataManipulateTest {
//
//
//    @Autowired
//    private CnFisParseRuleRelRepository cnFisParseRuleRelRepository;
//
//    @Test
//    public void testJpaQuery_findCnFisParseRuleRelsByEqpIdAndFileTyp(){
//
//        // Given
//        String eqpId = "eqp1";
//        CnFisParseRuleRel cnFisParseRuleRel = CnFisParseRuleRel.builder()
//                .eqpId(eqpId)
//                .fileTyp(String.valueOf(FisFileType.INSPECTION))
//                .fileClmVal("A")
//                .mpngClmNm("A_Name")
//                .build();
//
//        CnFisParseRuleRel saveEntity = cnFisParseRuleRelRepository.save(cnFisParseRuleRel);
//        log.info(saveEntity.toString());
//
//        List<CnFisParseRuleRel> allResult = cnFisParseRuleRelRepository.findAll();
//        log.info(allResult.toString());
//
//        List<CnFisParseRuleRel> resultList = cnFisParseRuleRelRepository.findCnFisParseRuleRelsByEqpIdAndFileTyp(
//                eqpId, FisFileType.INSPECTION);
//        log.info(resultList.toString());
//
//        assertThat(resultList).isNotNull();
//        assertThat(resultList).containsExactly(cnFisParseRuleRel);
//    }
//}
