select * from CN_FIS_PARSE_RULE
where 1=1
-- and EQP_ID = 'AM-YG-09-01'
  and FILE_TYP = 'INSPECTION';



insert into CN_FIS_PARSE_RULE (OBJ_ID, EQP_ID, FILE_TYP, FILE_TGT_POSN_VAL,
                               PARS_CLM_ID_VAL, PARS_ROW_VAL, CRT_DT, CRT_USER_ID, MDFY_DT,
                               MDFY_USER_ID, START_HDR_VAL)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a13', 'AP-TG-09-01', 'INSPECTION', 'Home/Path/To/Store/File',
        'A-W', '*',SYSDATE, 'David', SYSDATE,
        'David', 0
       );


insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_01', 'AP-TG-09-01', 'INSPECTION', 'A', 'SITE_ID','SITE_ID', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_02', 'AP-TG-09-01', 'INSPECTION', 'B', 'PROD_DEF_ID','PROD_DEF_ID', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_03', 'AP-TG-09-01', 'INSPECTION', 'C', 'PROC_DEF_ID','PROC_DEF_ID', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_04', 'AP-TG-09-01', 'INSPECTION', 'D', 'PROC_SGMT_ID','PROC_SGMT_ID', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_05', 'AP-TG-09-01', 'INSPECTION', 'E', 'EQP_ID','EQP_ID', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_06', 'AP-TG-09-01', 'INSPECTION', 'F', 'LOT_ID','LOT_ID', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_07', 'AP-TG-09-01', 'INSPECTION', 'G', 'PROD_MTRL_ID','PROD_MTRL_ID', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_08', 'AP-TG-09-01', 'INSPECTION', 'H', 'SUB_PROD_MTRL_ID','SUB_PROD_MTRL_ID', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_09', 'AP-TG-09-01', 'INSPECTION', 'I', 'MTRL_FACE_CD','MTRL_FACE_CD', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_10', 'AP-TG-09-01', 'INSPECTION', 'J', 'INSP_REPT_CNT','INSP_REPT_CNT', 'NUMBER',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_11', 'AP-TG-09-01', 'INSPECTION', 'K', 'STRIP_NO','STRIP_NO', 'NUMBER',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_12', 'AP-TG-09-01', 'INSPECTION', 'L', 'X_VAL','X_VAL', 'NUMBER',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_13', 'AP-TG-09-01', 'INSPECTION', 'M', 'Y_VAL','Y_VAL', 'NUMBER',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_14', 'AP-TG-09-01', 'INSPECTION', 'N', 'GRD_ID','GRD_ID', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_15', 'AP-TG-09-01', 'INSPECTION', 'O', 'DFCT_ID','DFCT_ID', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_16', 'AP-TG-09-01', 'INSPECTION', 'P', 'DFCT_X_VAL','DFCT_X_VAL', 'NUMBER',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_17', 'AP-TG-09-01', 'INSPECTION', 'Q', 'DFCT_Y_VAL','DFCT_Y_VAL', 'NUMBER',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_18', 'AP-TG-09-01', 'INSPECTION', 'R', 'INSP_DT','INSP_DT', 'TIMESTAMP',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_19', 'AP-TG-09-01', 'INSPECTION', 'S', 'IMG_FILE_NM','IMG_FILE_NM', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_20', 'AP-TG-09-01', 'INSPECTION', 'T', 'REVIEW_IMG_FILE_NM','REVIEW_IMG_FILE_NM', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_21', 'AP-TG-09-01', 'INSPECTION', 'U', 'INSP_FILE_NM','INSP_FILE_NM', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_22', 'AP-TG-09-01', 'INSPECTION', 'V', 'ATTR_1','ATTR_1', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_23', 'AP-TG-09-01', 'INSPECTION', 'W', 'ATTR_2','ATTR_2', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );


insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202412330111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_24', 'AP-TG-09-01', 'INSPECTION', 'W', 'ATTR_N','ATTR_N', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );