package com.bj.house.user.common;

/**
 * Created by BJ on 2018/1/26.
 */
public enum RestCode {

    OK(0,"OK"),
    UNKNOWN_ERROR(1,"服务异常"),
    TOKEN_INVALID(2,"Token失效"),
    USER_NOT_EXIST(3,"用户不存在"),
    WRONG_PAGE(10100,"页码不存在");

    public final int code;
    public final String msg;

    RestCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
