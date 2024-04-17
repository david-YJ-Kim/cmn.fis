package com.abs.cmn.fis.util.code;

public enum ProcessStateCode {
    R, // Receive message and create work status. (Default value)
    P, // Start read and parsing target file.
    I, // Complete parsing and start insert data.

    E, // Error while parsing file.
    IE, // Error while Insert data.
    PE, // Error while Parsing data.
    RE, // Error while Read data.
    S, // Complete send message to EDC.
    C // Complete application work (EDC will update work table).
    ;
}
