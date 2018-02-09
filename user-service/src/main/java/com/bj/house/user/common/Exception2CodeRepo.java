package com.bj.house.user.common;

import com.bj.house.user.exception.IllegalParamsException;
import com.bj.house.user.exception.UserException;
import com.bj.house.user.exception.WithTypeException;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.reflect.FieldUtils;

/**
 * Created by BJ on 2018/2/8.
 */
public class Exception2CodeRepo {

    //存储异常及状态Map
    private static final ImmutableMap<Object, RestCode> MAP = ImmutableMap.<Object, RestCode>builder()
            .put(IllegalParamsException.Type.WRONG_PAGE_NUM,RestCode.WRONG_PAGE) //页码信息错误
            .put(UserException.Type.USER_NOT_LOGIN,RestCode.TOKEN_INVALID) //用户未登录
            .put(IllegalStateException.class,RestCode.UNKNOWN_ERROR) //其他错误
            .build();

    private static Object getType(Throwable throwable){
        try {
            return FieldUtils.readDeclaredField(throwable,"type",true);
        }catch (Exception e){
            return null;
        }
    }

    public static RestCode getCode(Throwable throwable){
        if (throwable == null){
            return RestCode.UNKNOWN_ERROR;
        }
        Object target = throwable;
        if (throwable instanceof WithTypeException){
            Object type = getType(throwable);
            if (type != null){
                target = type;
            }
        }
        //如果在MAP中找到了异常
        RestCode restCode = MAP.get(target);
        if (restCode != null){
            return restCode;
        }
        //获取RootException->最底层的异常
        Throwable rootCause = ExceptionUtils.getRootCause(throwable);
        if (rootCause != null){
            return getCode(rootCause);
        }
        //如果RootException都没有返回，就返回未知异常
        return RestCode.UNKNOWN_ERROR;
    }

}
