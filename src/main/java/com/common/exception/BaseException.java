package com.common.exception;

public class BaseException extends RuntimeException {

    public BaseException(String exception) {
        super(exception);
    }

    public BaseException() {
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }
}

