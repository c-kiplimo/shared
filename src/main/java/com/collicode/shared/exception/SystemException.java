package com.collicode.shared.exception;

import lombok.Getter;

@Getter
public class SystemException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final String message;
    private String code = "400.99";

    private SystemException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    SystemException(String message) {
        this.message = message;
    }

}
