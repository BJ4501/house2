package com.bj.house.api.config;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

/**
 * Created by BJ on 2018/2/8.
 */
public class NewRuleConfig {

    @Autowired
    private IClientConfig ribbonClientConfig;

    //每隔10秒钟发送一次ping命令
    @Bean
    public IPing ribbonPing(IClientConfig config){
        return new PingUrl(false,"/health");
    }

    @Bean
    public IRule ribbonRule(IClientConfig config){
//        return new RandomRule(); 随机策略
        //LoadBalance拦截器执行之后，会对当前服务调用的成功或失败的结果进行记录
        //在下一次负载均衡时，会首先选择上一次成功的服务器，而不是最近服务调用失败的服务器
        return new AvailabilityFilteringRule();
    }



}
