package com.example.demo.exception;

import com.example.demo.entity.ApiResponse;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final Integer errorCode;
    private final Integer httpStatus;

    public BusinessException(String message) {
        super(message);
        this.errorCode = ApiResponse.BizCode.FAIL;
        this.httpStatus = 400;
    }

    public BusinessException(String message, Integer errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = 400;
    }

    public BusinessException(String message, Integer errorCode, Integer httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}