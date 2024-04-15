package com.example.notifyserver.common.exception.model;

import com.example.notifyserver.common.exception.enums.ErrorCode;

public class ConflictException extends NotifyException {
    public ConflictException(ErrorCode errorCode) {
        super(errorCode);
    }
}
