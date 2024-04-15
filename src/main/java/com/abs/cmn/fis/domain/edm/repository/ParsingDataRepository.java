package com.abs.cmn.fis.domain.edm.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.abs.cmn.fis.config.FisPropertyObject;
import com.abs.cmn.fis.domain.work.vo.ChFisWorkSaveRequestVo;
import com.abs.cmn.fis.util.FisCommonUtil;
import com.abs.cmn.fis.util.code.FisConstant;
import com.abs.cmn.fis.util.vo.ParseRuleVo;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class ParsingDataRepository {

    public String[] sqlColumList = null;

    static SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    static SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public String batchEntityInsert(String fileName, String workId, int startHeaderOffset, List<Map<String, String>> listMap, ParseRuleVo fileRule) {

        try {
            log.info("Data size: {} ", listMap.size());

            sqlColumList = fileRule.getMpngClmStrList();

            String query = fileRule.getQueryInsertBatch();

            int[] numberDataList = FisCommonUtil.convertToIntArray(fileRule.getNumberDataTypList());
            int[] timeStmpDataList = FisCommonUtil.convertToIntArray(fileRule.getTimeStampDataTypList());

            if (numberDataList != null && timeStmpDataList != null )
                log.info("Insert For File type : {}", fileRule.getFileTyp().name() +" , "+numberDataList.length + ","+timeStmpDataList.length);

            jdbcTemplate.setFetchSize(FisPropertyObject.getInstance().getBatchSize());

            jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
                @SneakyThrows
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {

                    log.info("CheckDataType SQL: {}", query);

                    Map<String, String> map = listMap.get(i);
                    ps.setString(1, FisCommonUtil.generateObjKey());
                    ps.setString(2, fileRule.getFileMoveDirectoryValue());
                    ps.setString(3, fileName);
                    ps.setString(4, workId);
                    ps.setInt(5, i + startHeaderOffset);

                    log.info("Colum List : {} ", Arrays.toString(sqlColumList));
                    for(int idx = 0; idx < sqlColumList.length; idx++){
                        try{
                            int addIdx = idx + 6;
//						log.info("Column:{}, Value: {}", sqlColumList[addIdx], map.getOrDefault(sqlColumList[addIdx], null));
                            if ( FisCommonUtil.checkDataInList(numberDataList, addIdx) ) {

                                log.info(addIdx+" , NUMBER " +sqlColumList[idx]+", "+map.getOrDefault(sqlColumList[idx], null));
                                ps.setInt(addIdx, Integer.valueOf( map.getOrDefault(sqlColumList[idx], "0")) );

                            } else if (FisCommonUtil.checkDataInList(timeStmpDataList, addIdx)){

                                log.info("CHECKDATEFORMAT columnName: {}, getInMap : {}",sqlColumList[idx], map.get(sqlColumList[idx]));

//							ps.setTimestamp(addIdx, Timestamp.valueOf(map.getOrDefault(sqlColumList[idx], null)));

                                Date date = inputFormat.parse(map.get(sqlColumList[idx]));
                                String formattedDate = outputFormat.format(date);
                                ps.setTimestamp(addIdx, Timestamp.valueOf(formattedDate));


//                                ps.setTimestamp(addIdx, Timestamp.valueOf(FisCommonUtil.convertDateFormat(map.getOrDefault(sqlColumList[idx], null))));

                                log.info(addIdx+" , TIME " +sqlColumList[idx]+", "+map.getOrDefault(sqlColumList[idx], null));

                            } else {

                                ps.setString(addIdx, map.getOrDefault(sqlColumList[idx], null));
                                log.info(addIdx+" , STR " +sqlColumList[idx]+", "+map.getOrDefault(sqlColumList[idx], null));
                            }
                        }catch (Exception e){

                            e.printStackTrace();
                            log.error(e.getMessage());
                            log.error("Error Colum Element : {}", sqlColumList[idx]);
                            throw e;
                        }
                    }

                }

                @Override
                public int getBatchSize() {
                    return listMap.size();
                }
            });
            log.info("Batch Complete. maxRows: {}", jdbcTemplate.getMaxRows());
            return "Complete";

        } catch (Exception vo) {
            vo.printStackTrace();
            log.error("## ", vo);
        }

        return workId;
    }



    public String deleteBatch(String fileType, String key, int batchSize, String sql) throws SQLException {

        log.info("in deleteBatch() FILE_TYPE : "+fileType);
        log.info("in deleteBatch() OBJ_ID : "+key);
        log.info("in deleteBatch() batchSize : "+batchSize);
        log.info("# deleteBatch sql : "+sql);

        int[] deletedRowNum = null;

        BatchPreparedStatementSetter batchSetter = new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, key);
            }

            @Override
            public int getBatchSize() {
                return batchSize;
            }
        };
        jdbcTemplate.setFetchSize(batchSize);

        deletedRowNum = jdbcTemplate.batchUpdate(sql, batchSetter);

        log.debug("# ParsingDataRepository-deleteBatch() / deletedRowNum : "+Arrays.toString(deletedRowNum));

        if ( deletedRowNum.length < 0)
            return FisConstant.DELETE_FAIL.name();
        else
            return FisConstant.DELETE_BATCH.name();
    }

    public List<Map<String, Object>> getDeleteEntities(String sql){
        List<Map<String,Object>> rst = jdbcTemplate.queryForList(sql);
        return rst;
    }

    public boolean deleteCnWork(String objId) {

        String sql = FisPropertyObject.getInstance().getDeleteCnWork();
        log.info("[-] sql Delete objId : "+sql+" , "+objId);
        int rst = jdbcTemplate.update(sql, objId);
        log.info("[-] sql Delete cnt : "+rst);
        if (rst > 0 ) return true;
        else return false;

    }

    public void insertChWork(ChFisWorkSaveRequestVo row) {
        String query = FisPropertyObject.getInstance().getInsertWorkHistory();
        jdbcTemplate.update(query
                , row.getObjId()
                , row.getRefObjId()
                , row.getFileName()
                , row.getFilePath()
                , row.getFileType()
                , row.getEqpId()
                , row.getRequestSystemName()
                , row.getProcessState()
                , row.getCreateUserId()
                , row.getUpdateUserId()
        );
        log.info("Seuccess : " + row.getRefObjId());
    }

}
