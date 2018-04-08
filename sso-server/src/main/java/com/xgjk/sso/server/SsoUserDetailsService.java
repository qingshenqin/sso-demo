package com.xgjk.sso.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SsoUserDetailsService implements UserDetailsService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        UserDetails userDetails = null;
        String sql = "select n.username,n.password from users n where n.enabled=1 and n.username=? ";
        Object[] list_param = new Object[1];
        list_param[0] = username;


        Map<String, Object> map_e = null;
        try {
            map_e = jdbcTemplate.queryForMap(sql, list_param);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.print("map_e : ");
        System.out.println(map_e);
        if (map_e != null && !map_e.isEmpty()) {
//            log.info("密码karl："+passwordEncoder.encode(map_e.get("password").toString()));
            userDetails = new User(map_e.get("username").toString(),passwordEncoder.encode(map_e.get("password").toString()),AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
        }

        return userDetails;
//        return new User(username,passwordEncoder.encode("123456"), AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
    }
}
