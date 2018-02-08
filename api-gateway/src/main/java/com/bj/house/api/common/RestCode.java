package com.bj.house.api.common;

/**
 * Created by BJ on 2018/1/26.
 */
public enum RestCode {

    OK(0,"OK"),
    UNKNOWN_ERROR(1,"服务异常"),
    WRONG_PAGE(10100,"页码不存在");

    public final int code;
    public final String msg;

    RestCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
