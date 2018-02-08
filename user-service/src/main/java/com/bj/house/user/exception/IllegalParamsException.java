package com.bj.house.user.exception;

/**
 * Created by BJ on 2018/2/8.
 */
public class IllegalParamsException extends RuntimeException implements WithTypeException{
    //WithTypeException 暂只做标识用

    private Type type;

    public IllegalParamsException(Type type,String msg){
        super(msg);
        this.type = type;
    }

    public enum Type{
        WRONG_PAGE_NUM,WRONG_TYPE
    }

}
