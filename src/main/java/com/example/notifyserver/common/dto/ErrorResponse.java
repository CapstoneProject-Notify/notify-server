package com.example.notifyserver.common.dto;

import com.example.notifyserver.common.exception.enums.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse extends ApiResponse{
    private final int code;
    private final String message;

    public static ErrorResponse error(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getHttpStatus().value(), errorCode.getMessage());
    }

    public static ErrorResponse badRequestError(final String errorMessage) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage);
    }
}