package com.bj.house.api.common;

/**
 * Created by BJ on 2018/1/26.
 */
public class RestResponse<T> {

    private int code;
    private String message;
    private T result;

    public RestResponse(){
        this(RestCode.OK.code,RestCode.OK.msg);
    }

    public RestResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> RestResponse<T> success(){
        RestResponse<T> response = new RestResponse<T>();
        return response;
    }

    public static <T> RestResponse<T> success(T result){
        RestResponse<T> response = new RestResponse<T>();
        response.setResult(result);
        return response;
    }

    public static <T> RestResponse<T> error(RestCode code){
        return new RestResponse<T>(code.code,code.msg);
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
