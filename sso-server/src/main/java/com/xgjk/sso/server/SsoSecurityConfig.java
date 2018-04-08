package com.xgjk.sso.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

/*
* WebSecurityConfigurerAdapter 配置web的请求要不要验证
* 以及配置告诉认证服务器哪里取用户的信息，密码加密器
* */
@Configuration
public class SsoSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;

    //    密码加密
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .and().jdbcAuthentication()
                .passwordEncoder(passwordEncoder())
                .dataSource(dataSource);

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        formLogin()配置使用spring默认的表单登录，不配置，使用spring的安全认证弹出框。
        http.formLogin().and().authorizeRequests().anyRequest().authenticated();
    }
}
