package com.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class BaseNotFoundException extends BaseException {

    public BaseNotFoundException(String exception) {
        super(exception);
    }

    public BaseNotFoundException() {
    }

    public BaseNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
