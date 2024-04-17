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

import com.abs.cmn.fis.config.FisSharedInstance;
import com.abs.cmn.fis.domain.work.vo.ChFisWorkSaveRequestVo;
import com.abs.cmn.fis.util.FisCommonUtil;
import com.abs.cmn.fis.util.code.FisConstant;
import com.abs.cmn.fis.util.vo.ParseRuleVo;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class ParsingDataRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public String batchEntityInsert(String trackingKey, String fileName, String workId, int startHeaderOffset, List<Map<String, String>> listMap, ParseRuleVo fileRule) {

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            log.info("{} Data size: {} ", trackingKey, listMap.size());

            String[] sqlColumList = fileRule.getMpngClmStrList();

            String query = fileRule.getQueryInsertBatch();

            int[] numberDataList = FisCommonUtil.convertToIntArray(fileRule.getNumberDataTypList());
            int[] timeStmpDataList = FisCommonUtil.convertToIntArray(fileRule.getTimeStampDataTypList());

            if (numberDataList != null && timeStmpDataList != null )
                log.info("{} Insert For File type : {}", trackingKey,  fileRule.getFileTyp().name() +" , "+numberDataList.length + ","+timeStmpDataList.length);

            jdbcTemplate.setFetchSize(FisSharedInstance.getInstance().getBatchSize());

            jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
                @SneakyThrows
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {

                    log.debug("{} CheckDataType SQL: {}",trackingKey, query);

                    Map<String, String> map = listMap.get(i);
                    ps.setString(1, FisCommonUtil.generateObjKey());
                    ps.setString(2, fileRule.getFileMoveDirectoryValue());
                    ps.setString(3, fileName);
                    ps.setString(4, workId);
                    ps.setInt(5, i + startHeaderOffset);

                    log.debug("{} Colum List : {} ", trackingKey, Arrays.toString(sqlColumList));
                    for(int idx = 0; idx < sqlColumList.length; idx++){
                        try{
                            int addIdx = idx + 6;
                            if ( FisCommonUtil.checkDataInList(numberDataList, addIdx) ) {

                                log.debug("{} Row number:{}, type: {}, key: {}, value: {}", trackingKey, addIdx, "Number", sqlColumList[idx], map.getOrDefault(sqlColumList[idx], null));
                                ps.setInt(addIdx, Integer.valueOf( map.getOrDefault(sqlColumList[idx], "0")) );

                            } else if (FisCommonUtil.checkDataInList(timeStmpDataList, addIdx)){

                                log.debug("{} Row number:{}, type: {}, key: {}, value: {}", trackingKey, addIdx, "Timestamp", sqlColumList[idx], map.getOrDefault(sqlColumList[idx], null));
                                Date date = inputFormat.parse(map.get(sqlColumList[idx]));
                                String formattedDate = outputFormat.format(date);
                                ps.setTimestamp(addIdx, Timestamp.valueOf(formattedDate));

                            } else {

                                log.debug("{} Row number:{}, type: {}, key: {}, value: {}", trackingKey, addIdx, "String", sqlColumList[idx], map.getOrDefault(sqlColumList[idx], null));
                                ps.setString(addIdx, map.getOrDefault(sqlColumList[idx], null));
                            }
                        }catch (Exception e){

                            log.error("{} Error row number: {} , Colum Element : {}. Value: {}, Error :{} ", trackingKey,  idx, sqlColumList[idx], map.getOrDefault(sqlColumList[idx], null), e);
                            throw e;
                        }
                    }

                }

                @Override
                public int getBatchSize() {
                    return listMap.size();
                }
            });
            log.info("{} Batch Complete. maxRows: {}", trackingKey, jdbcTemplate.getMaxRows());
            return "Complete";

        } catch (Exception vo) {
            vo.printStackTrace();
            log.error("{} Error: {}",trackingKey, vo);
            throw vo;
        }

    }



    public String deleteBatch(String trackingKey, String fileType, String key, int batchSize, String sql) throws SQLException {

        log.info("{} Delete process start. " +
                        "fileType: {}, objectId: {}, batchSize: {}, deleteSql: {}",
                trackingKey, fileType, key, batchSize, sql);

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

        log.debug("{} # ParsingDataRepository-deleteBatch() / deletedRowNum :{}", trackingKey, Arrays.toString(deletedRowNum));

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

        String sql = FisSharedInstance.getInstance().getDeleteCnWork();
        log.info("[-] sql Delete objId : "+sql+" , "+objId);
        int rst = jdbcTemplate.update(sql, objId);
        log.info("[-] sql Delete cnt : "+rst);
        if (rst > 0 ) return true;
        else return false;

    }

    public void insertChWork(ChFisWorkSaveRequestVo row) {
        String query = FisSharedInstance.getInstance().getInsertWorkHistory();
        jdbcTemplate.update(query
//                , row.getObjId()
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
