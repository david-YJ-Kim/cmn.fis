package com.abs.cmn.fis.util.code;

public enum FisConstant {

   R,
   local,
   remote,

   receiver,

    A, 				// 파싱 컬럼 정보 기준 숫자
    FAIL,
    SUCCESS,

    ParsingFaile,	// 파싱 실패 롤백 명령

    InfoTribe,		// 메세지로 송신된 파일 정보가 부족

    Inpection,		// 검사
    Measure,		// 계측 , 측정 의미

    DELETE_BATCH,		// rollback
    DELETE_FAIL,	// rollback Fail
    sftp,
    body,
    head,
    fileName,
    fileType,
    filePath,
    eqpId
    ;
}
