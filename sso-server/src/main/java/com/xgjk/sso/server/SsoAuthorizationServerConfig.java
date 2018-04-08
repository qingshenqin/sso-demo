package com.xgjk.sso.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class SsoAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
//    哪些客户端是我认证服务器认为安全的
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("xgjk1")
                .secret("xgjkSecret1")
                .authorizedGrantTypes("authorization_code", "refresh_token")
                .scopes("all")
                .and()
                .withClient("xgjk2")
                .secret("xgjkSecret2")
                .authorizedGrantTypes("authorization_code", "refresh_token")
                .scopes("all");
    }

    //
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(jwtTokenStore()).accessTokenConverter(jwtAccessTokenConverter());
    }

//    认证服务器的安全服务器


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
// 访问认证服务器的tokenKey需要经过身份认证,tokenKey为converter.setSigningKey("xgjk")中的xgjk.那为什么需要token，因为返回去的是JWT，
// jwt是经过认证服务器签名的，应用需要拿到tokenkey才能够验证，解析token的正确性，默认TokenKey是拒绝的。
        security.tokenKeyAccess("isAuthenticated()");
    }

    //配置发什么令牌
    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    //jwt签名，令牌的增强器，转换器
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("xgjk");
        return converter;
    }



}
