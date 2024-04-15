package com.example.notifyserver.common.exception.model;

import com.example.notifyserver.common.exception.enums.ErrorCode;

public class BadRequestException extends NotifyException {
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
