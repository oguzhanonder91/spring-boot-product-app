package com.util.enums;

public enum MethodType {
    GET ("GET"),
    PUT ("PUT"),
    POST ("POST"),
    DELETE("DELETE"),
    OPTIONS("OPTIONS");

    private String value;

    MethodType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
