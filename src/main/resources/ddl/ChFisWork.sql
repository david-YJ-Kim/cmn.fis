-- MESADM.CH_FIS_WORK definition

CREATE TABLE "MESADM"."CH_FIS_WORK"
(	"OBJ_ID" VARCHAR2(100) NOT NULL ENABLE,
     "REF_OBJ_ID" VARCHAR2(100) NOT NULL ENABLE,
     "FILE_NM" VARCHAR2(100) NOT NULL ENABLE,
     "FILE_PATH" VARCHAR2(100),
     "FILE_TYPE" VARCHAR2(100),
     "EQP_ID" VARCHAR2(100),
     "REQ_SYS_NM" VARCHAR2(20) NOT NULL ENABLE,
     "PROC_STATE" VARCHAR2(100) DEFAULT 'R',
     "CREATE_USER_ID" VARCHAR2(40) NOT NULL ENABLE,
     "CREATE_DATE" TIMESTAMP (6) NOT NULL ENABLE,
     "UPDATE_USER_ID" VARCHAR2(40),
     "UPDATE_DATE" TIMESTAMP (6),
     CONSTRAINT "CH_FIS_WORK_PK" PRIMARY KEY ("OBJ_ID")
);


COMMENT ON TABLE MESADM.CH_FIS_WORK IS '파일인터페이스워크히스토리테이블';
COMMENT ON COLUMN MESADM.CH_FIS_WORK.OBJ_ID IS '워크ID';
COMMENT ON COLUMN MESADM.CH_FIS_WORK.REF_OBJ_ID IS '참조워크ID';
COMMENT ON COLUMN MESADM.CH_FIS_WORK.FILE_NM IS '파일이름';
COMMENT ON COLUMN MESADM.CH_FIS_WORK.FILE_PATH IS '파일경로';
COMMENT ON COLUMN MESADM.CH_FIS_WORK.FILE_TYPE IS '파일타입';
COMMENT ON COLUMN MESADM.CH_FIS_WORK.REQ_SYS_NM IS '요청시스템이름';
COMMENT ON COLUMN MESADM.CH_FIS_WORK.PROC_STATE IS '처리상태[수신완료: R / 파싱입력: I / MOS처리: M / MOS처리 완료: C / 장애: D]';
COMMENT ON COLUMN MESADM.CH_FIS_WORK.CREATE_USER_ID IS '입력자';
COMMENT ON COLUMN MESADM.CH_FIS_WORK.CREATE_DATE IS '입력일시';
COMMENT ON COLUMN MESADM.CH_FIS_WORK.UPDATE_USER_ID IS '수정자';
COMMENT ON COLUMN MESADM.CH_FIS_WORK.UPDATE_DATE IS '수정일시';