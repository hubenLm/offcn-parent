package com.offcn.dycommon.response;

import com.offcn.dycommon.enums.ResponseEnums;
/**
 * 应用统一返回结果数据封装类
 * @author Administrator
 * @param <T> 返回结果数据类型
 */
public class AppResponse<T> {
    private Integer code;
    private String msg;
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 快速响应成功
     * @param data
     * @return
     */

    public static<T> AppResponse<T> ok(T data){
        AppResponse<T> response = new AppResponse<T>();
        response.setCode(ResponseEnums.SUCCESS.getCode());
        response.setMsg(ResponseEnums.SUCCESS.getMessage());
        response.setData(data);
        return response;
    }

    public static<T> AppResponse<T> fail(T data){
        AppResponse<T> response = new AppResponse<T>();
        response.setCode(ResponseEnums.FAIL.getCode());
        response.setMsg(ResponseEnums.FAIL.getMessage());
        response.setData(data);
        return response;
    }
}
