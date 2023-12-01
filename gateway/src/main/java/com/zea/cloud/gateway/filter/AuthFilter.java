package com.zea.cloud.gateway.filter;

//import com.zea.cloud.common.constant.HeaderConstant;
//import com.zea.cloud.common.utils.JwtUtil;
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
public class AuthFilter implements GlobalFilter {

//    @Value("${nofilter.url}")
//    private String noFilter;

    @Value("${auth.enable}")
    private boolean authEnable;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if(!authEnable){
            return chain.filter(exchange);
        }
        int x = 1/0;

        /*String path = exchange.getRequest().getPath().toString();
        if (noFilter.contains(path)) {
            return chain.filter(exchange);
        }
        List<String> headers = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if(headers == null || headers.isEmpty()){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        String accessToken = headers.get(0);
        if(StringUtils.hasLength(accessToken)){
            accessToken = accessToken.replace(HeaderConstant.BEARER + " ","");
            if (!JwtUtil.verifyAccessToken(accessToken)) {
                //设置返回认证失效，前端发送refreshToken 重新申请 accessToken
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        }*/
        return chain.filter(exchange);
    }
}
