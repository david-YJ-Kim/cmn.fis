select * from CN_FIS_IF_PARSE_RULE_REL
where 1=1
-- and REF_OBJ_ID = '20231130111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a17'
  and REF_OBJ_ID = '202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a13'
order by FILE_CLM_VAL
;

select * from CN_FIS_IF_PARSE_RULE
where 1=1
-- and EQP_ID = 'AM-YG-09-01'
  and FILE_TYP = 'MEAS';



insert into CN_FIS_IF_PARSE_RULE (OBJ_ID, EQP_ID, FILE_TYP, FILE_TGT_POSN_VAL,
                                  PARS_CLM_ID_VAL, PARS_ROW_VAL, CRT_DT, CRT_USER_ID, MDFY_DT,
                                  MDFY_USER_ID, START_HDR_VAL)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a13', 'AP-TG-04-01', 'MEAS', 'Home/Path/To/Store/File',
        'A-W', '*',SYSDATE, 'David', SYSDATE,
        'David', 0
       );


insert into CN_FIS_IF_PARSE_RULE_REL (OBJ_ID, REF_OBJ_ID,
                                      FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                      CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_01', '202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a13',
        'A', 'SITE_ID','SITE_ID', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_IF_PARSE_RULE_REL (OBJ_ID, REF_OBJ_ID,
                                      FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                      CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_02', '202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a13',
        'B', 'PROD_DEF_ID','PROD_DEF_ID', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_IF_PARSE_RULE_REL (OBJ_ID, REF_OBJ_ID,
                                      FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                      CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_03', '202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a13',
        'C', 'PROC_DEF_ID','PROC_DEF_ID', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_IF_PARSE_RULE_REL (OBJ_ID, REF_OBJ_ID,
                                      FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                      CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_04', '202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a13',
        'D', 'PROC_SGMT_ID','PROC_SGMT_ID', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_IF_PARSE_RULE_REL (OBJ_ID, REF_OBJ_ID,
                                      FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                      CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_05', '202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a13',
        'E', 'EQP_ID','EQP_ID', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_IF_PARSE_RULE_REL (OBJ_ID, REF_OBJ_ID,
                                      FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                      CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_06', '202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a13',
        'F', 'LOT_ID','LOT_ID', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_IF_PARSE_RULE_REL (OBJ_ID, REF_OBJ_ID,
                                      FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                      CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_07', '202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a13',
        'G', 'PROD_MTRL_ID','PROD_MTRL_ID', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_IF_PARSE_RULE_REL (OBJ_ID, REF_OBJ_ID,
                                      FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                      CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_08', '202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a13',
        'H', 'SUB_PROD_MTRL_ID','SUB_PROD_MTRL_ID', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_IF_PARSE_RULE_REL (OBJ_ID, REF_OBJ_ID,
                                      FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                      CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_09', '202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a13',
        'I', 'MTRL_FACE_CD','MTRL_FACE_CD', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_IF_PARSE_RULE_REL (OBJ_ID, REF_OBJ_ID,
                                      FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                      CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_10', '202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a13',
        'J', 'INSP_REPT_CNT','INSP_REPT_CNT', 'NUMBER',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_IF_PARSE_RULE_REL (OBJ_ID, REF_OBJ_ID,
                                      FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                      CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_11', '202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a13',
        'K', 'STRIP_NO','STRIP_NO', 'NUMBER',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_IF_PARSE_RULE_REL (OBJ_ID, REF_OBJ_ID,
                                      FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                      CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_12', '202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a13',
        'L', 'X_VAL','X_VAL', 'NUMBER',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_IF_PARSE_RULE_REL (OBJ_ID, REF_OBJ_ID,
                                      FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                      CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_13', '202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a13',
        'M', 'Y_VAL','Y_VAL', 'NUMBER',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_IF_PARSE_RULE_REL (OBJ_ID, REF_OBJ_ID,
                                      FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                      CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_14', '202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a13',
        'N', 'GRD_ID','GRD_ID', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_IF_PARSE_RULE_REL (OBJ_ID, REF_OBJ_ID,
                                      FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                      CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_15', '202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a13',
        'O', 'DFCT_ID','DFCT_ID', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_IF_PARSE_RULE_REL (OBJ_ID, REF_OBJ_ID,
                                      FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                      CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_16', '202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a13',
        'P', 'DFCT_X_VAL','DFCT_X_VAL', 'NUMBER',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_IF_PARSE_RULE_REL (OBJ_ID, REF_OBJ_ID,
                                      FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                      CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_17', '202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a13',
        'Q', 'DFCT_Y_VAL','DFCT_Y_VAL', 'NUMBER',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_IF_PARSE_RULE_REL (OBJ_ID, REF_OBJ_ID,
                                      FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                      CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_18', '202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a13',
        'R', 'INSP_DT','INSP_DT', 'TIMESTAMP',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_IF_PARSE_RULE_REL (OBJ_ID, REF_OBJ_ID,
                                      FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                      CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_19', '202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a13',
        'S', 'IMG_FILE_NM','IMG_FILE_NM', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_IF_PARSE_RULE_REL (OBJ_ID, REF_OBJ_ID,
                                      FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                      CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_20', '202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a13',
        'T', 'REVIEW_IMG_FILE_NM','REVIEW_IMG_FILE_NM', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_IF_PARSE_RULE_REL (OBJ_ID, REF_OBJ_ID,
                                      FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                      CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_21', '202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a13',
        'U', 'INSP_FILE_NM','INSP_FILE_NM', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_IF_PARSE_RULE_REL (OBJ_ID, REF_OBJ_ID,
                                      FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                      CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_22', '202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a13',
        'V', 'ATTR_1','ATTR_1', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_IF_PARSE_RULE_REL (OBJ_ID, REF_OBJ_ID,
                                      FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                      CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_23', '202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a13',
        'W', 'ATTR_2','ATTR_2', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );


insert into CN_FIS_IF_PARSE_RULE_REL (OBJ_ID, REF_OBJ_ID,
                                      FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                      CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_24', '202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a13',
        'W', 'ATTR_N','ATTR_N', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );