package com.bj.house.user.controller;

import com.bj.house.user.common.RestResponse;
import com.bj.house.user.exception.IllegalParamsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by BJ on 2018/1/26.
 */
@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${server.port}")
    private Integer port;

    @RequestMapping("getusername")
    public RestResponse<String> getUsername(Long id){
        logger.info("IncommingRequest,port:" + port);
        if (id == null){
            throw new IllegalParamsException(IllegalParamsException.Type.WRONG_PAGE_NUM,"错误分页");
        }
        return RestResponse.success("test:"+port);
    }

}
