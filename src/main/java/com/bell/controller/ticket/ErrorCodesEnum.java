package com.bell.controller.ticket;

public enum ErrorCodesEnum {
	TICKET_CLOSED(1000),
	TICKET_NOT_FOUND(1002),
    FIELD_MISSING(1003),
    BAD_FORMAT(1004),
    SERVER_ERROR(1005);
 
    public final int code;
 
    private ErrorCodesEnum(int code) {
        this.code = code;
    }
    
}
