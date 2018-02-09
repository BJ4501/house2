package com.bj.house.user.exception;

/**
 * 用户相关异常
 * Created by BJ on 2018/2/9.
 */
public class UserException extends RuntimeException implements WithTypeException {

//    private static final long

    private Type type;

    public UserException(Type type,String message) {
        super(message);
        this.type = type;
    }

    public Type type(){
        return type;
    }

    public enum Type{
        USER_NOT_LOGIN,
        USER_NOT_FOUND,
        USER_AUTH_FAIL; //用户鉴权失败
    }

}
