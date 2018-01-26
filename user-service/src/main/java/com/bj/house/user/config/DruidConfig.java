package com.bj.house.user.config;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.google.common.collect.Lists;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Druid 配置项
 * Created by BJ on 2018/1/19.
 */
@Configuration
public class DruidConfig {

    //NOTE：ConfigurationProperties，
    //1.将外部配置文件(application.properties)与Bean数据关系进行绑定
    //2.将Bean方法中的返回对象，与外部的配置文件进行绑定
    @ConfigurationProperties(prefix = "spring.druid")
    @Bean(initMethod = "init",destroyMethod = "close")//启动与关闭的操作
    public DruidDataSource dataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setProxyFilters(Lists.newArrayList(statFilter()));
        return dataSource;
    }

    @Bean
    public Filter statFilter(){
        StatFilter filter = new StatFilter();
        //约定时间1000ms ：超过1000ms的查询，认为是慢日志
        filter.setSlowSqlMillis(1000);
        //是否打印出日志
        filter.setLogSlowSql(true);
        //是否将日志合并
        filter.setMergeSql(true);
        return filter;
    }

    //添加监控功能
    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        return new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
    }


}
