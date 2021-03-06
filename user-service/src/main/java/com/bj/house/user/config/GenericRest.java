package com.bj.house.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 封装RestTemplate
 * 1.支持直连访问
 * 2.构建请求更容易
 * Created by BJ on 2018/1/26.
 */
@Service
public class GenericRest {

    @Autowired
    private RestTemplate lbRestTemplate;

    @Autowired
    private RestTemplate directRestTemplate;

    private static final String directFlag = "direct://";

    public <T> ResponseEntity<T> post(String url, Object reqBody, ParameterizedTypeReference<T> responseType){
        RestTemplate template = getRestTemplate(url);
        //将url标识移除
        url = url.replace(directFlag,"");
        return template.exchange(url, HttpMethod.POST, new HttpEntity<Object>(reqBody),responseType);
    }

    public <T> ResponseEntity<T> get(String url, ParameterizedTypeReference<T> responseType){
        RestTemplate template = getRestTemplate(url);
        //将url标识移除
        url = url.replace(directFlag,"");
        return template.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, responseType);
    }

    private RestTemplate getRestTemplate(String url) {
        if (url.contains(directFlag))
            return directRestTemplate;
        else
            return lbRestTemplate;
    }


}
