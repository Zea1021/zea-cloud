package com.zea.cloud.common.exception;

import lombok.Getter;

@Getter
public class MyException extends RuntimeException{

    private final ErrorCode errorCode;
    private final String code;
    private final String message;

    public MyException() {
        super();
        this.errorCode = ErrorCode.SYSTEM_EXCEPTION;
        this.code = ErrorCode.SYSTEM_EXCEPTION.getCode();
        this.message = ErrorCode.SYSTEM_EXCEPTION.getDesc();
    }

    public MyException(ErrorCode errorCode) {
        super(errorCode.getDesc());
        this.errorCode = errorCode;
        this.code = errorCode.getCode();
        this.message = errorCode.getDesc();
    }

    public MyException(ErrorCode errorCode, String message) {
        super(errorCode.getDesc() + ":" + message);
        this.errorCode = errorCode;
        this.code = errorCode.getCode();
        this.message = message;
    }
}
