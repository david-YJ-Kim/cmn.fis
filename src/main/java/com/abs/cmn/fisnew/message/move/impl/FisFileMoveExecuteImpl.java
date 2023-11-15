package com.abs.cmn.fisnew.message.move.impl;

import com.abs.cmn.fisnew.message.move.FisFileMoveExecute;
import com.abs.cmn.fisnew.util.service.FileManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FisFileMoveExecuteImpl implements FisFileMoveExecute {

    @Autowired
    private FileManager fileManager;

    @Override
    public void init() {

    }

    @Override
    public String execute(String fileType, String fileName, String filePath, String workId) throws Exception {
        return null;
    }
}

