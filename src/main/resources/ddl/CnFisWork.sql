-- MESADM.CN_FIS_WORK definition

CREATE TABLE "MESADM"."CN_FIS_WORK"
(	"OBJ_ID" VARCHAR2(100) NOT NULL ENABLE,
     "FILE_NM" VARCHAR2(100) NOT NULL ENABLE,
     "TRACKING_KEY" VARCHAR2(100),
     "FILE_PATH" VARCHAR2(500),
     "FILE_TYPE" VARCHAR2(100),
     "EQP_ID" VARCHAR2(100),
     "REQ_SYS_NM" VARCHAR2(20) NOT NULL ENABLE,
     "PROC_STATE" VARCHAR2(10) DEFAULT 'R',
     "CREATE_USER_ID" VARCHAR2(40) NOT NULL ENABLE,
     "CREATE_DATE" TIMESTAMP (6) NOT NULL ENABLE,
     "UPDATE_USER_ID" VARCHAR2(40),
     "UPDATE_DATE" TIMESTAMP (6),
     CONSTRAINT "CN_FIS_WORK_PK" PRIMARY KEY ("OBJ_ID")
);

COMMENT ON TABLE MESADM.CN_FIS_WORK IS '파일인터페이스워크테이블';
COMMENT ON COLUMN MESADM.CN_FIS_WORK.OBJ_ID IS '워크ID';
COMMENT ON COLUMN MESADM.CN_FIS_WORK.TRACKING_KEY IS '로그 트래킹 키';
COMMENT ON COLUMN MESADM.CN_FIS_WORK.FILE_NM IS '파일이름';
COMMENT ON COLUMN MESADM.CN_FIS_WORK.FILE_PATH IS '파일경로';
COMMENT ON COLUMN MESADM.CN_FIS_WORK.FILE_TYPE IS '파일타입';
COMMENT ON COLUMN MESADM.CN_FIS_WORK.EQP_ID IS '장비아이디';
COMMENT ON COLUMN MESADM.CN_FIS_WORK.REQ_SYS_NM IS '요청시스템이름';
COMMENT ON COLUMN MESADM.CN_FIS_WORK.PROC_STATE IS '처리상태[수신완료: R / 파싱입력: I / MOS처리: M / MOS처리 완료: C / 장애: D]';
COMMENT ON COLUMN MESADM.CN_FIS_WORK.CREATE_USER_ID IS '입력자';