-- MESADM.CN_FIS_IF_PARSE_RULE definition

CREATE TABLE "MESADM"."CN_FIS_PARSE_RULE"
(	"OBJ_ID" VARCHAR2(100) NOT NULL ENABLE,
     "EQP_ID" VARCHAR2(40) NOT NULL ENABLE,
     "FILE_TYP" VARCHAR2(40) NOT NULL ENABLE,
     "FILE_TGT_POSN_VAL" VARCHAR2(255) NOT NULL ENABLE,
     "PARS_CLM_ID_VAL" VARCHAR2(255) NOT NULL ENABLE,
     "PARS_ROW_VAL" VARCHAR2(255) NOT NULL ENABLE,
     "CRT_DT" TIMESTAMP (6) DEFAULT SYSTIMESTAMP NOT NULL ENABLE,
     "CRT_USER_ID" VARCHAR2(40) DEFAULT 'FIS' NOT NULL ENABLE,
     "MDFY_DT" TIMESTAMP (6) DEFAULT SYSTIMESTAMP,
     "MDFY_USER_ID" VARCHAR2(40),
     "START_HDR_VAL" NUMBER(38,0) DEFAULT 0 NOT NULL ENABLE,
     CONSTRAINT "CN_FIS_PARSING_FILE_INFO_PK" PRIMARY KEY ("OBJ_ID"),
     CONSTRAINT "CN_FIS_PARSING_FILE_INFO_UN" UNIQUE ("EQP_ID", "FILE_TYP")
);

COMMENT ON TABLE MESADM.CN_FIS_PARSE_RULE IS '파싱 파일 정보 보관 테이블';
COMMENT ON COLUMN MESADM.CN_FIS_PARSE_RULE.OBJ_ID IS '오브젝트ID';
COMMENT ON COLUMN MESADM.CN_FIS_PARSE_RULE.EQP_ID IS '장비이름';
COMMENT ON COLUMN MESADM.CN_FIS_PARSE_RULE.FILE_TYP IS '파일유형';
COMMENT ON COLUMN MESADM.CN_FIS_PARSE_RULE.FILE_TGT_POSN_VAL IS '파일 타겟 위치 값';
COMMENT ON COLUMN MESADM.CN_FIS_PARSE_RULE.PARS_CLM_ID_VAL IS '파싱헤더아이디값';
COMMENT ON COLUMN MESADM.CN_FIS_PARSE_RULE.PARS_ROW_VAL IS '파싱행값';
COMMENT ON COLUMN MESADM.CN_FIS_PARSE_RULE.CRT_DT IS '생성일시';
COMMENT ON COLUMN MESADM.CN_FIS_PARSE_RULE.CRT_USER_ID IS '생성자아이디';
COMMENT ON COLUMN MESADM.CN_FIS_PARSE_RULE.MDFY_DT IS '수정일시';
COMMENT ON COLUMN MESADM.CN_FIS_PARSE_RULE.MDFY_USER_ID IS '수정자아이디';
COMMENT ON COLUMN MESADM.CN_FIS_PARSE_RULE.START_HDR_VAL IS '파싱 시작 위치';