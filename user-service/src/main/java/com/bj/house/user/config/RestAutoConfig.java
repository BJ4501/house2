package com.bj.house.user.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import org.apache.http.client.HttpClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Arrays;


/**
 * Created by BJ on 2018/1/26.
 */
@Configuration
public class RestAutoConfig {

    public static class RestTemplateConfig{

        //支持LoadBalanced
        @Bean
        @LoadBalanced //Spring对restTemplate bean 进行定制，加入LoadBalance 拦截器进行ip：port的替换
        RestTemplate lbRestTemplate(HttpClient httpClient){
            RestTemplate template = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
            //默认传输使用iso8859，修改成utf-8模式，兼容中文
            template.getMessageConverters().add(0,new StringHttpMessageConverter(Charset.forName("utf-8")));
            //template.getMessageConverters().add(1,new FastJsonHttpMessageConverter());
            template.getMessageConverters().add(1,new FastJsonHttpMessageConverter5());
            return template;
        }

        //直连访问
        @Bean
        RestTemplate directRestTemplate(HttpClient httpClient){
            RestTemplate template = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
            //默认传输使用iso8859，修改成utf-8模式，兼容中文
            template.getMessageConverters().add(0,new StringHttpMessageConverter(Charset.forName("utf-8")));
            //template.getMessageConverters().add(1,new FastJsonHttpMessageConverter());
            template.getMessageConverters().add(1,new FastJsonHttpMessageConverter5());
            return template;
        }

        /**
         * 解决FastJson在Spring的Bug
         * 在FastJson中的MediaType.ALL，Spring会将MediaType处理成字节流类型，而不会处理成JSON格式
         * Created by BJ on 2018/1/26.
         */
        public static class FastJsonHttpMessageConverter5 extends FastJsonHttpMessageConverter4{

            public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

            public FastJsonHttpMessageConverter5(){
                setDefaultCharset(DEFAULT_CHARSET);
                setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON,
                        new MediaType("application","*+json")));
            }
        }

    }


}
