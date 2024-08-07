insert into CN_FIS_PARSE_RULE (OBJ_ID, EQP_ID, FILE_TYP, FILE_TGT_POSN_VAL,
                               PARS_CLM_ID_VAL, PARS_ROW_VAL, CRT_DT, CRT_USER_ID, MDFY_DT,
                               MDFY_USER_ID, START_HDR_VAL)
values ('202404180111351133-d5f90065-8923-4cfd-b18d-94e7c3d8d89a13', 'AP-PD-11-01', 'MEASUREMENT', 'Home/Path/To/Store/File',
        'A-W', '*',SYSDATE, 'David', SYSDATE,
        'David', 0
       );


insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202404180111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_01', 'AP-PD-11-01', 'MEASUREMENT', 'A', 'SITE_ID','SITE_ID', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202404180111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_02', 'AP-PD-11-01', 'MEASUREMENT', 'B', 'PROD_DEF_ID','PROD_DEF_ID', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202404180111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_03', 'AP-PD-11-01', 'MEASUREMENT', 'C', 'PROC_DEF_ID','PROC_DEF_ID', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202404180111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_04', 'AP-PD-11-01', 'MEASUREMENT', 'D', 'PROC_SGMT_ID','PROC_SGMT_ID', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202404180111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_05', 'AP-PD-11-01', 'MEASUREMENT', 'E', 'EQP_ID','EQP_ID', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202404180111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_06', 'AP-PD-11-01', 'MEASUREMENT', 'F', 'LOT_ID','LOT_ID', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202404180111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_07', 'AP-PD-11-01', 'MEASUREMENT', 'G', 'PROD_MTRL_ID','PROD_MTRL_ID', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202404180111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_08', 'AP-PD-11-01', 'MEASUREMENT', 'H', 'SUB_PROD_MTRL_ID','SUB_PROD_MTRL_ID', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202404180111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_09', 'AP-PD-11-01', 'MEASUREMENT', 'I', 'MTRL_FACE_CD','MTRL_FACE_CD', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202404180111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_10', 'AP-PD-11-01', 'MEASUREMENT', 'J', 'INSP_REPT_CNT','INSP_REPT_CNT', 'NUMBER',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202404180111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_11', 'AP-PD-11-01', 'MEASUREMENT', 'K', 'STRIP_NO','STRIP_NO', 'NUMBER',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202404180111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_12', 'AP-PD-11-01', 'MEASUREMENT', 'L', 'X_VAL','X_VAL', 'NUMBER',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202404180111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_13', 'AP-PD-11-01', 'MEASUREMENT', 'M', 'Y_VAL','Y_VAL', 'NUMBER',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202404180111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_14', 'AP-PD-11-01', 'MEASUREMENT', 'N', 'POS_X_VAL','PX_VAL', 'NUMBER',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202404180111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_15', 'AP-PD-11-01', 'MEASUREMENT', 'O', 'POS_Y_VAL','PY_VAL', 'NUMBER',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202404180111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_16', 'AP-PD-11-01', 'MEASUREMENT', 'P', 'POS_Z_VAL','PZ_VAL', 'NUMBER',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202404180111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_17', 'AP-PD-11-01', 'MEASUREMENT', 'Q', 'DCITEM_ID','DCITEM_ID', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202404180111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_18', 'AP-PD-11-01', 'MEASUREMENT', 'R', 'RSLT_VAL','RSLT_VAL', 'NUMBER',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202404180111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_19', 'AP-PD-11-01', 'MEASUREMENT', 'S', 'INSP_DT','INSP_DT', 'TIMESTAMP',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202404180111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_20', 'AP-PD-11-01', 'MEASUREMENT', 'T', 'IMG_FILE_NM','IMG_FILE_NM', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202404180111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_21', 'AP-PD-11-01', 'MEASUREMENT', 'U', 'INSP_FILE_NM','INSP_FILE_NM', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202404180111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_22', 'AP-PD-11-01', 'MEASUREMENT', 'V', 'ATTR_1','ATTR_1', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );

insert into CN_FIS_PARSE_RULE_REL (OBJ_ID, EQP_ID, FILE_TYP, FILE_CLM_VAL, FILE_CLM_NM,MPNG_CLM_NM, CLM_DATA_TYP,
                                   CRT_DT, CRT_USER_ID, MDFY_DT, MDFY_USER_ID)
values ('202404180111351133-d5f90065-8923-4cfd-b18d-94e7c3d89a_23', 'AP-PD-11-01', 'MEASUREMENT', 'W', 'ATTR_2','ATTR_2', 'VARCHAR2',
        SYSDATE, 'David', SYSDATE, 'David'
       );


