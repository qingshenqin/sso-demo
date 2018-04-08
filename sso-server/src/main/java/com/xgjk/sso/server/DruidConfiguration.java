package com.xgjk.sso.server;

import com.alibaba.druid.filter.logging.LogFilter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DruidConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource(LogFilter logFilter) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.getProxyFilters().add(logFilter);
        return druidDataSource;
    }

    @Bean
    public LogFilter logFilter() {
        LogFilter logFilter = new Slf4jLogFilter();
        logFilter.setStatementLogEnabled(true);
        logFilter.setResultSetLogEnabled(true);
        return logFilter;
    }
}
