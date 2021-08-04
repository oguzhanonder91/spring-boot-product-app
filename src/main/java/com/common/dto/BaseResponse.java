package com.common.dto;

import java.io.Serializable;

public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;

    private String message;

    private String key ;

    private T data;

    public BaseResponse(String message, String key, T data) {
        this.message = message;
        this.key = key;
        this.data = data;
    }

    public BaseResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
