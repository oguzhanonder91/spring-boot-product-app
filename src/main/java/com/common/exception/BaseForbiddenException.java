package com.common.exception;

public class BaseForbiddenException extends BaseException {

    public BaseForbiddenException(String exception) {
        super(exception);
    }

    public BaseForbiddenException() {
    }

    public BaseForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}
