package com.absolics.value;

public enum FISValues {
	
	FAIL,
	SUCCESS,
	
	ParsingFaile,	// 파싱 실패 롤백 명령
	
	InfoTribe,		// 메세지로 송신된 파일 정보가 부족
	
	Inpection,		// 검사
	Measure,		// 계측 , 측정 의미
	
	ROLLBACK,		// rollback
	ROLLBACK_FAIL	// rollback Fail 	
	;
}
