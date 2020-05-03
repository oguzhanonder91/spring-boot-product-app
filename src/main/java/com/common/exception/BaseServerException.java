package com.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class BaseServerException extends BaseException {

    public BaseServerException(String exception) {
        super(exception);
    }

    public BaseServerException() {
    }

    public BaseServerException(String message, Throwable cause) {
        super(message, cause);
    }
}

