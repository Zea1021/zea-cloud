package com.zea.cloud.gateway.filter;

import com.zea.cloud.tool.enums.TokenEnum;
import com.zea.cloud.tool.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class GatewayAuthFilter implements GlobalFilter {

    @Value("${no-filter.url}")
    private String noFilter;

    @Value("${auth.enable}")
    private boolean authEnable;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 是否需要认证
        if(!authEnable){
            return chain.filter(exchange);
        }
        // 该请求是否不用过滤
        String path = exchange.getRequest().getPath().toString();
        if (noFilter.contains(path)) {
            return chain.filter(exchange);
        }
        // 判断请求头是否含有 AUTHORIZATION 字段且是否为空
        List<String> headers = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if(headers == null || headers.isEmpty() || !StringUtils.hasLength(headers.get(0))){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        // 获取请求头中的token值
        String accessToken = headers.get(0);
        // 校验token及其类型
        if (!JwtUtil.verifyToken(accessToken) ||
                !JwtUtil.getTokenType(accessToken).equals(TokenEnum.ACCESS_TOKEN.getType())) {
            // 设置返回认证失效，此时前端需要再次使用refreshToken来刷新token
            // 如果refreshToken也过期了则需要重新登录获取token
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }
}
