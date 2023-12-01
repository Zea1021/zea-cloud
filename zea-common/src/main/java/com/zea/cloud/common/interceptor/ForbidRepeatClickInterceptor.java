package com.zea.cloud.common.interceptor;

import com.zea.cloud.common.annotation.ForbidRepeatClick;
import com.zea.cloud.common.bean.entity.User;
import com.zea.cloud.common.exception.ErrorCode;
import com.zea.cloud.common.exception.MyException;
import com.zea.cloud.common.utils.JwtUtil;
import com.zea.cloud.common.utils.RedisUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class ForbidRepeatClickInterceptor implements HandlerInterceptor {

    @Resource
    private RedisUtil redisUtil;

    /**
     * preHandle方法是用来做权限验证的，如果验证通过，返回true，否则返回false
     * @param request     request
     * @param response    response
     * @param handler     handler
     * @return            true/false
     * @throws Exception  Exception
     */
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        // @ForbidRepeatClick注解作用在方法上，所以拦截器仅需要拦截方法
        if (handler instanceof HandlerMethod handlerMethod) {
            ForbidRepeatClick forbidRepeatClick = handlerMethod.getMethodAnnotation(ForbidRepeatClick.class);
            if (forbidRepeatClick != null) {
                int time = forbidRepeatClick.time();
                TimeUnit timeUnit = forbidRepeatClick.timeUnit();
                String message = forbidRepeatClick.message();

                String token = request.getHeader("Authorization");
                User user = JwtUtil.getUserInfo(token);
//                if (user.getId() == null) {
//                    throw new MyException(ErrorCode.UN_AUTH);
//                }
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
                        throw new RuntimeException(message);
                    }
                }
                redisUtil.setKeyValue(redisKey, currentTime, time, timeUnit);
            }
        }
        return true;
    }

}
