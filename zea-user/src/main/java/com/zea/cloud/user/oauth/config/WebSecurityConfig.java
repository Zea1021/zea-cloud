package com.zea.cloud.user.oauth.config;

import com.zea.cloud.user.oauth.filter.JWTAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author  jfzou
 * @since   2024/02/18
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    //自定义异常认证处理
    private final JwtAuthEntryPoint authEntryPoint;

    //自定义授权异常处理
    private final MyAccessDeniedHandler myAccessDeniedHandler;

    // 自定义用户认证逻辑

    public WebSecurityConfig(JwtAuthEntryPoint authEntryPoint, MyAccessDeniedHandler myAccessDeniedHandler) {
        this.authEntryPoint = authEntryPoint;
        this.myAccessDeniedHandler = myAccessDeniedHandler;
    }

    // 配置需要放行的接口
    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return new WebSecurityCustomizer() {
            @Override
            public void customize(WebSecurity web) {
                /*// 不能写成/zea-cloud/zea-user/auth/**，正确写法为/auth/** 或者 auth/**
                web.ignoring().requestMatchers(HttpMethod.POST, "/auth/**");*/
                web.ignoring().requestMatchers(HttpMethod.POST, "/auth/loginByForm", "/auth/refreshToken");
            }
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 禁用basic明文验证
                .httpBasic().disable()

                // 配置csrf，没有disable()且没有token会报错:
                // Forbidden:Could not verify the provided CSRF token because no token was found to compare. 或者
                // Forbidden:Invalid CSRF Token 'null' was found on the request parameter '_csrf' or header 'X-CSRF-TOKEN'.
                .csrf().disable()

                // 配置自定义鉴权处理器
                .exceptionHandling()
                .accessDeniedHandler(myAccessDeniedHandler)
                .authenticationEntryPoint(authEntryPoint)
                .and()

                // 配置session创建策略为不创建
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // 配置表单认证全部放行
                .formLogin().permitAll()
                .and()

                .authorizeHttpRequests()
                // 配置需要放行的请求，这里的配置等同于 WebSecurityCustomizer bean 中配置的放行路径
                .requestMatchers(HttpMethod.POST, "/auth/loginByForm", "/auth/refreshToken").permitAll()
                // 允许 SpringMVC 的默认错误地址匿名访问
                .requestMatchers("/error").permitAll()

                // 配置接口的用户角色权限
                // 含有 /admin/ 的url需要具备 ROLE_ADMIN 或者 ROLE_DBA 权限才能访问
                // 注意 hasRole 方法没有使用 "ROLE_" 前缀，因为其会自动追加前缀并和数据中的role信息进行比较
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/admin/**").hasAnyRole("ADMIN", "DBA")
                // 注意 hasAuthority 方法有使用 "ROLE_" 前缀
                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_DBA")
                // todo 添加同时需要多个用户角色的接口进行测试

                // 除了上面放行的路径，所有的http请求必须通过授权认证才可以访问，
                .anyRequest().authenticated()
                .and()

                // 将 JWTAuthenticationFilter 设置在 UsernamePasswordAuthenticationFilter 之前
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /*
    // 密码加密,由于数据库密码已经使用md5进行了加密,这里再进行加密会导致校验失败
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    */

    // 自定义 认证过滤器
    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }

}
