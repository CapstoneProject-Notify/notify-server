package com.example.notifyserver.common.exception.model;

import com.example.notifyserver.common.dto.TokenPair;
import com.example.notifyserver.common.exception.enums.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundUserException extends NotifyException {
    private final TokenPair tokenPair;

    public NotFoundUserException(ErrorCode errorCode, TokenPair tokenPair) {
        super(errorCode);
        this.tokenPair = tokenPair;
    }
}
