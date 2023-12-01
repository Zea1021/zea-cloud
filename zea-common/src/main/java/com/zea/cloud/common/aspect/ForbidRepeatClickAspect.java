package com.zea.cloud.common.aspect;

import com.zea.cloud.common.annotation.ForbidRepeatClick;
import com.zea.cloud.common.bean.entity.User;
import com.zea.cloud.common.exception.ErrorCode;
import com.zea.cloud.common.exception.MyException;
import com.zea.cloud.common.utils.JwtUtil;
import com.zea.cloud.common.utils.RedisUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 切面类：禁止短时间内重复点击
 */
@Slf4j
@Aspect
@Component
public class ForbidRepeatClickAspect {

    @Resource
    private HttpServletRequest request;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 切入点：增强标有@ForbidRepeatClick注解的方法
     */
    @Pointcut(value = "@annotation(com.zea.cloud.common.annotation.ForbidRepeatClick)")
    public void pointcut() {
    }

    /**
     * 切入方法 前置增强
     * @param joinPoint 切入点对象
     */
    @Before("pointcut()")
    public void forbidRepeatClick(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        ForbidRepeatClick forbidRepeatClick = method.getAnnotation(ForbidRepeatClick.class);
        if (forbidRepeatClick != null) {
            int time = forbidRepeatClick.time();
            TimeUnit timeUnit = forbidRepeatClick.timeUnit();
            String message = forbidRepeatClick.message();
            String token = request.getHeader("Authorization");
            User user = JwtUtil.getUserInfo(token);
//            if (user.getId() == null) {
//                throw new MyException(ErrorCode.UN_AUTH);
//            }
            String redisKey = "ForbidRepeatClick:"
                    + request.getRequestURI().trim()
                    + ":"
                    + request.getMethod()
                    + ":userId:"
                    +  user.getId();
            Long previousTime = redisUtil.getKeyValue(redisKey);
            Long currentTime = new Date().getTime();
            if (previousTime != null) {
                if (currentTime - previousTime < timeUnit.toMillis(time)) {
                    // 重复点击
                    throw new MyException(ErrorCode.BUSINESS_EXCEPTION, message);
                }
            }
            redisUtil.setKeyValue(redisKey, currentTime, time, timeUnit);
        }
    }
}
