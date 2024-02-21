package com.zea.cloud.common.exception;

import com.zea.cloud.common.bean.common.Result;
import com.zea.cloud.common.utils.ResultUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class, Controller.class})
public class MyExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, ValidationException.class})
    private Result<?> handleValidationException(Exception ex) {
        // 如果ex是ConstraintViolationException，则换转为该异常并赋值为exception
        // ConstraintViolationException异常继承自ValidationException
        if (ex instanceof ConstraintViolationException exception){
            Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
            String msg = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("; "));
            log.error(ex.getMessage(), ex);
            return ResultUtil.fail(ErrorCode.VALID_EXCEPTION, msg);
        } else if (ex instanceof MethodArgumentNotValidException exception) {
            // todo MethodArgumentNotValidException继承自BindException，这里可以删除，因为下面已经处理了BindException
            String msg = exception.getBindingResult().getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining("; "));
            log.error(ex.getMessage(), ex);
            return ResultUtil.fail(ErrorCode.VALID_EXCEPTION, msg);
        } else if (ex instanceof BindException exception) {
            String msg = exception.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining("; "));
            log.error(ex.getMessage(), ex);
            return ResultUtil.fail(ErrorCode.VALID_EXCEPTION, msg);
        }
        log.error(ex.getMessage(), ex);
        return ResultUtil.fail(ErrorCode.VALID_EXCEPTION, ex.getMessage());
    }

    @ExceptionHandler({MyLockException.class})
    public Result<?> handleMyLockException(MyLockException ex) {
        log.error(ex.getMessage(), ex);
        return ResultUtil.fail(ErrorCode.LOCK_ERROR, ex.getMessage());
    }

    @ExceptionHandler(MyException.class)
    private Result<?> handleMyException(MyException ex) {
        log.error(ex.getMessage(), ex);
        return ResultUtil.fail(ex.getErrorCode(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    private Result<?> handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ResultUtil.fail(ErrorCode.SYSTEM_EXCEPTION);
    }
}
