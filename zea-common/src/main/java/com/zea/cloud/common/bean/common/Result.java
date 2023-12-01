package com.zea.cloud.common.bean.common;

import com.zea.cloud.common.exception.ErrorCode;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

    /**
     * 错误代码
     */
    private String code;
    /**
     * 错误信息
     */
    private String message;
    /**
     * 返回数据
     */
    private T data;

    /**
     * feignClient返回值为复杂对象时其类型必须有无参构造函数。
     * 因此这里给Result添加上无参构造器
     */
    public Result() {
    }

    public Result(String code, String message) {
        this.code = code;
        this.message = message;
    }
    public Result(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getDesc();
    }

    public Result(ErrorCode errorCode, String message) {
        this.code = errorCode.getCode();
        this.message = errorCode.getDesc();
        if (StringUtils.hasLength(message)) {
            this.message = message;
        }
    }

    public Result(ErrorCode errorCode, T data) {
        this.code = errorCode.getCode();
        this.message = errorCode.getDesc();
        this.data = data;
    }

}
