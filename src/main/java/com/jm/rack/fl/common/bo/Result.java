package com.jm.rack.fl.common.bo;

import java.io.Serializable;

public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 成功
     */
    public static final int SUCCESS = 0;

    /**
     * 失败
     */
    public static final int FAIL = 500;

    private int code;

    private String msg;

    private T data;
    private String result;

    public static <T> Result<T> success() {
        return restResult(null, SUCCESS, "操作成功");
    }

    public static <T> Result<T> success(T data) {
        return restResult(data, SUCCESS, "操作成功");
    }

    public static <T> Result<T> success(T data, String msg) {
        return restResult(data, SUCCESS, msg);
    }

    public static <T> Result<T> error() {
        return restResult(null, FAIL, "操作失败");
    }

    public static <T> Result<T> error(String msg) {
        return restResult(null, FAIL, msg);
    }

    public static <T> Result<T> error(T data) {
        return restResult(data, FAIL, "操作失败");
    }

    public static <T> Result<T> error(T data, String msg) {
        return restResult(data, FAIL, msg);
    }

    public static <T> Result<T> error(int code, String msg) {
        return restResult(null, code, msg);
    }

    private static <T> Result<T> restResult(T data, int code, String msg) {
        Result<T> apiResult = new Result<>();
        apiResult.setCode(code);
        apiResult.setResult(code == SUCCESS ? "success" : "fail");
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
