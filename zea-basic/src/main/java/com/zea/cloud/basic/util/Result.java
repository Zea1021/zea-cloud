package com.zea.cloud.basic.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description: Web接口返回信息
 * @author: jfzou
 * @date: 2023/9/22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Result implements Serializable {
    public static final int SUCC_CODE = 200;
    public static final int FAIL_CODE = 9999;
    public static final String SUCC_STATUS = "succ";
    public static final String FAIL_STATUS = "fail";
    /**
     * 返回码
     * - 200 正常返回
     * - 9999 异常返回
     */
    private int code;
    /**
     * 返回状态
     * - succ : 成功
     * - fail : 失败
     */
    private String status;
    /**
     * 返回异常信息
     * - 200 为空
     * - 9999 异常信息
     */
    private String msg;
    /**
     * 返回数据
     */
    private Object data;

    /**
     * 不带数据的成功调用
     *
     * @return
     */
    public static Result okWithOutData() {
        return Result.builder()
                .code(SUCC_CODE)
                .status(SUCC_STATUS)
                .msg("")
                .build();
    }

    /**
     * 不带数据的异常调用
     *
     * @param message
     * @return
     */
    public static Result errorWithOutData(String message) {
        return Result.builder()
                .code(FAIL_CODE)
                .status(FAIL_STATUS)
                .msg(message)
                .build();
    }

    /**
     * 带数据不支持分页的成功调用
     *
     * @param val
     * @return
     */
    public static Result okWithDataNoPage(Object val) {
        return Result.builder()
                .code(SUCC_CODE)
                .status(SUCC_STATUS)
                .data(val)
                .build();
    }
    public static Result okWithDataNoPage(String message,Object val) {
        return Result.builder()
                .code(SUCC_CODE)
                .status(SUCC_STATUS)
                .data(val)
                .msg(message)
                .build();
    }
}
