package com.example.notifyserver.common.exception.model;

import com.example.notifyserver.common.exception.enums.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundUserException extends NotifyException {

    public NotFoundUserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
