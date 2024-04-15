package com.abs.cmn.fis.util.code;

public enum ProcessStateCode {
    R, // Receive message and create work status. (Default value)
    P, // Start read and parsing target file.
    I, // Complete parsing and start insert data.
    C // Complete application work.
    ;
}
