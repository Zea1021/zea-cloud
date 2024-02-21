package com.zea.cloud.user.oauth.filter;

import com.zea.cloud.tool.constant.HttpConstant;
import com.zea.cloud.tool.enums.TokenEnum;
import com.zea.cloud.tool.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private UserDetailsService myUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 获取请求头中的 token 信息
        String token = getJwtTokenFromRequest(request);
        // 校验 token
        if (StringUtils.hasLength(token) && JwtUtil.verifyToken(token) &&
                JwtUtil.getTokenType(token).equals(TokenEnum.ACCESS_TOKEN.getType())) {
            // 解析 token 中的用户信息 （用户的唯一标识）
            String username = JwtUtil.getUserName(token);
            // 获取用户信息
            UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                    userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // 认证成功存储认证信息到上下文缓存中,这样后续可以通过 SecurityContextHolder 拿到当前登录用户信息，完成后续过滤器校验
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(context);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 获取jwt token信息
     *
     * @param request   request
     * @return          JwtToken
     */
    private String getJwtTokenFromRequest(HttpServletRequest request) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(StringUtils.hasLength(accessToken)){
            return accessToken.replace(HttpConstant.BEARER + " ","");
        }
        return null;
    }
}