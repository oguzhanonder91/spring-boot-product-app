package com.common.dto;

import java.io.Serializable;
import java.util.List;

public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;

    private String message;

    private String key ;

    private List<T> dataList;

    private T data;

    public BaseResponse(String message, String key, List<T> dataList, T data) {
        this.message = message;
        this.key = key;
        this.dataList = dataList;
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

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
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
