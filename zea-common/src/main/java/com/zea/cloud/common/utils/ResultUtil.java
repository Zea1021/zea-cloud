package com.zea.cloud.common.utils;

import com.zea.cloud.common.bean.common.Result;
import com.zea.cloud.common.exception.ErrorCode;

public class ResultUtil {

    /**
     * 成功
     */
    public static <T> Result<T> success() {
        return new Result<>(ErrorCode.SUCCESS);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ErrorCode.SUCCESS, data);
    }

    /**
     * 失败
     */
    public static <T> Result<T> fail(ErrorCode errorCode) {
        return new Result<>(errorCode);
    }

    public static <T> Result<T> fail(ErrorCode errorCode, String message) {
        return new Result<>(errorCode, message);
    }
}
