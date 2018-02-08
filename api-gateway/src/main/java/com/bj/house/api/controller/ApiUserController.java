package com.bj.house.api.controller;

import com.bj.house.api.common.RestResponse;
import com.bj.house.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by BJ on 2018/1/26.
 */
@RestController
public class ApiUserController {

    @Autowired
    private UserService userService;

    @RequestMapping("test/getusername")
    public RestResponse<String> getUsername(Long id){
        return RestResponse.success(userService.getUsername(id));
    }

}
