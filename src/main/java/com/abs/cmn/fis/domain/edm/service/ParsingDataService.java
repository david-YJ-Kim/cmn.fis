//package com.abs.cmn.fis.domain.edm.service;
//
//import java.util.List;
//import java.util.Map;
//
//import org.json.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.abs.cmn.fis.domain.edm.repository.ParsingDataRepository;
//import com.abs.cmn.fis.util.code.FisConstant;
//import com.abs.cmn.fis.vo.ParsingRuleVo;
//
//public class ParsingDataService {
//	private static final Logger log = LoggerFactory.getLogger(ParsingDataService.class);
//
//
//	@Autowired
//	private ParsingDataRepository repository;
//
//	//TODO : Repository 에서 동시에 진행하던 service 부분을 구현 진행
//	//TODO : work ID 어디서 진행?
//
//	public JSONObject insertParsingDatas(String fileType, List<Map<String, String>> parsData, ParsingRuleVo rule) {
//		String res = null;
//
//		log.info("# @@ insert to oracle db after parsing !! ");
//		if (fileType.equals(FisConstant.Inpection.name()))
//			res = repository.batchInsert(FisConstant.Inpection.name(), parsData, rule);
//		else
//			res = repository.batchInsert(FisConstant.Measure.name(), parsData, rule);
//
//		// 파일이 입력이 실패하면 rollback
//		if ( res.equals(FisConstant.SUCCESS.name()) ) {
//
//			// work id 로 모두 삭제
//			repository.rollback(parsData.get(0).toString());
//
//		// 파일 제대로 잘 저장 되었다면, work table에 update 로직  수행
//		} else {
//			;
//		}
//
//		// toString 으로 solace MSG 에 넣을 수 있는 구조로 메세지 포멧 setting 해서 return
//		return setResultFormat("", "", "", "");
//	}
//
//	private JSONObject setResultFormat(String workId, String result, String filePath, String fileName) {
//		return null;
//	}
//}