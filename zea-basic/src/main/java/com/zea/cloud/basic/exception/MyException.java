package com.zea.cloud.basic.exception;

import com.zea.cloud.basic.constant.ErrorCode;

public class MyException extends RuntimeException{

    private String msgCode;
    private String msg;
    private String module;

    public MyException() {
        super();
    }

    public MyException(ErrorCode errorCode) {
        super(errorCode.getDesc());
        this.msg = errorCode.getDesc();
        this.msgCode = errorCode.getIndex();
    }

    public MyException(ErrorCode errorCode, String msg) {
        super(msg == null ? errorCode.getDesc() : errorCode.getDesc() + ":" + msg);
        this.msg = msg == null ? errorCode.getDesc() : msg;
        this.msgCode = errorCode.getIndex();
    }

    public MyException(ErrorCode errorCode, String module, String msg) {
        super(msg == null ? errorCode.getDesc() : errorCode.getDesc() + ":" + msg);
        this.msg = msg == null ? errorCode.getDesc() : msg;
        this.msgCode = errorCode.getIndex();
        this.module = module;
    }

}
