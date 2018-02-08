package com.bj.house.api.dao;

import com.bj.house.api.common.RestResponse;
import com.bj.house.api.config.GenericRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Repository;

/**
 * Created by BJ on 2018/1/26.
 */
@Repository
public class UserDao {

    @Autowired
    private GenericRest rest;

    public String getUsername(Long id){
        String url = "http://user/getusername?id="+id;
        //String url = "direct://http://127.0.0.1:8083/getusername?id="+id;
        RestResponse<String> response = rest.get(url, new ParameterizedTypeReference<RestResponse<String>>() {}).getBody();
        return response.getResult();
    }

}
