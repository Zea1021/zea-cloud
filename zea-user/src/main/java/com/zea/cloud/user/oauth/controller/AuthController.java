package com.zea.cloud.user.oauth.controller;

import com.zea.cloud.common.bean.LoginCondition;
import com.zea.cloud.common.bean.common.Result;
import com.zea.cloud.common.bean.entity.LoginUserInfo;
import com.zea.cloud.common.exception.ErrorCode;
import com.zea.cloud.common.exception.MyException;
import com.zea.cloud.common.utils.ResultUtil;
import com.zea.cloud.common.utils.UserContextUtil;
import com.zea.cloud.tool.constant.HttpConstant;
import com.zea.cloud.tool.utils.JwtUtil;
import com.zea.cloud.tool.utils.Md5Util;
import com.zea.cloud.user.bean.condition.RefreshCondition;
import com.zea.cloud.user.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("auth")
public class AuthController {

    // oauth认证服务
    @Resource
    private AuthenticationManager authenticationManager;
    // 用户服务
    @Resource
    private UserService userService;

    /**
     * 表单登录
     * @param loginCondition    登录入参
     * @return                  用户登录信息
     */
    @PostMapping("loginByForm")
    public Result<LoginUserInfo> loginByForm(@RequestBody @Validated LoginCondition loginCondition){
        log.info("登录认证开始, username: {}", loginCondition.getUserName());
        // todo 校验图片验证码
        // 将用户填写的信息映射到 UsernamePasswordAuthenticationToken
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginCondition.getUserName(), Md5Util.encodeByMD5(loginCondition.getPassword()));

        // 认证校验，校验失败会抛出异常，无异常表示校验成功
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 认证成功存储认证信息到上下文
        // 官网原文：你应该创建一个空的 SecurityContext 实例，而不是使用
        // SecurityContextHolder.getContext().setAuthentication(authentication)，以避免多线程之间的竞争。
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        // SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("登录认证完成, username: {}", loginCondition.getUserName());

        // 返回用户登录信息
        LoginUserInfo login = userService.login(loginCondition);

        return ResultUtil.success(login);
    }

    /**
     * 表单登录
     * @return  注销
     */
    @GetMapping("loginOut")
    public Result<?> loginOut(HttpServletRequest request){
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasLength(token)) {
            throw new MyException(ErrorCode.UN_AUTH);
        }
        token = token.replace(HttpConstant.BEARER + " ", "");
        String userId = JwtUtil.getUserId(token);
        log.info("开始注销, userId: {}", userId);
        // 删除redis缓存的session信息，此时前端需同步清除浏览器缓存的token信息
        UserContextUtil.removeCurrentUser();
        log.info("注销完成, userId: {}", userId);
        return ResultUtil.success();
    }

    /**
     * 获取当前登录用户信息
     * @return  当前登录用户信息
     */
    @GetMapping("getCurrentUserInfo")
    public Result<LoginUserInfo> getCurrentUserInfo(){
        LoginUserInfo currentUserInfo = UserContextUtil.getCurrentUserInfo();
        return ResultUtil.success(currentUserInfo);
    }

    /**
     * 刷新token
     * @param condition  刷新条件
     * @return           用户信息 + token
     */
    @PostMapping("refreshToken")
    public Result<LoginUserInfo> refreshToken(@RequestBody RefreshCondition condition) {
        LoginUserInfo loginUserInfo = userService.refreshToken(condition);
        return ResultUtil.success(loginUserInfo);
    }

}
