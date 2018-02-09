package com.bj.house.user.controller;

import com.bj.house.user.common.RestResponse;
import com.bj.house.user.model.User;
import com.bj.house.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by BJ on 2018/1/26.
 */
@RestController
@RequestMapping("user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    //----查询----

    //根据用户id
    @RequestMapping("getById")
    public RestResponse<User> getUserById(Long id){
        User user = userService.getUserById(id);
        return RestResponse.success(user);
    }

    //根据用户属性
    @RequestMapping("getList") //@RequestBody 接收一个json串，进行反序列化
    public RestResponse<List<User>> getUserList(@RequestBody User user){
        List<User> users = userService.getUserByQuery(user);
        return RestResponse.success(users);
    }

    //----注册----

    @RequestMapping("add")
    public RestResponse<User> add(@RequestBody User user){
        userService.addAccount(user,user.getEnableUrl());
        return RestResponse.success();
    }

    //激活
    @RequestMapping("enable")
    public RestResponse<User> enable(String key){
        userService.enable(key);
        return RestResponse.success();
    }



}
