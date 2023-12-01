package com.zea.cloud.common.exception;

import com.zea.cloud.common.bean.common.Result;
import com.zea.cloud.common.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class, Controller.class})
public class MyExceptionHandler {

    @ExceptionHandler(Exception.class)
    private Result<?> handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return ResultUtil.fail(ErrorCode.SYSTEM_EXCEPTION);
    }

    @ExceptionHandler(MyException.class)
    private Result<?> handleMyException(MyException myException) {
        log.error(myException.getMessage(), myException);
        return ResultUtil.fail(myException.getErrorCode(), myException.getMessage());
    }
}
