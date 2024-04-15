package com.example.notifyserver.common.exception.model;

import com.example.notifyserver.common.exception.enums.ErrorCode;

public class NotFoundException extends NotifyException{
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
