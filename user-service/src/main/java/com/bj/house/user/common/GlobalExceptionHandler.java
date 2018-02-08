package com.bj.house.user.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * 异常处理
 * Created by BJ on 2018/2/8.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ResponseStatus(HttpStatus.OK) //200
    @ExceptionHandler(value = Throwable.class) //定义为处理异常的方法
    @ResponseBody //序列化为JSON
    public RestResponse<Object> handler(HttpServletRequest req, Throwable throwable){
        LOGGER.error(throwable.getMessage(),throwable);
        RestCode restCode = Exception2CodeRepo.getCode(throwable);
        RestResponse<Object> response = new RestResponse<>(restCode.code,restCode.msg);
        return response;
    }

}
