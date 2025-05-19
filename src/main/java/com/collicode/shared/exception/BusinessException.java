package com.collicode.shared.exception;

import java.util.Objects;

import static com.collicode.shared.exception.ExceptionCode.DUPLICATE;
import static com.collicode.shared.exception.ExceptionCode.GENERALUTIL;


public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final String code;
    private final String key;
    private final String message;

    private BusinessException(String code, String key, String message) {
        this.code = code;
        this.key = key;

        this.message = Objects.nonNull(message) ? message : " KEY=" + key + ", CODE=" + code;
    }


    public static BusinessException exception(String code) {
        return new BusinessException(code, null, null);
    }

    public static BusinessException exception(String code, String key) {
        return new BusinessException(code, key, null);
    }

    public static BusinessException exceptionWithKey(String code, String key) {
        return new BusinessException(code, key, null);
    }

    public static BusinessException exceptionWithMessage(String code, String message) {
        return new BusinessException(code, GENERALUTIL, message);
    }

    public static BusinessException duplicateKeyException(String key) {
        return new BusinessException(DUPLICATE, key, null);
    }

    public String getCode() {
        return code;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
