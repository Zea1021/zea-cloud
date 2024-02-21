package com.zea.cloud.common.exception;

public class MyLockException extends MyException{

    public MyLockException() {
        super(ErrorCode.LOCK_ERROR);
    }

    public MyLockException(ErrorCode errorCode) {
        super(errorCode);
    }

    public MyLockException(ErrorCode errorCode, String msg) {
        super(errorCode, msg);
    }
}
