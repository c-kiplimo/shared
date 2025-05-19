package com.collicode.shared.util;


import com.collicode.shared.exception.BusinessEntityException;
import com.collicode.shared.exception.BusinessException;
import org.springframework.util.StringUtils;

import static com.collicode.shared.exception.ExceptionCode.GENERALUTIL;

public class BusinessValidation {

    public static BusinessValidation newInstance() {
        return new BusinessValidation();
    }

    public static void ifTrue(boolean condition, String code) {
        if (condition) {
            throw BusinessException.exception(code);
        }
    }

    public static void ifTrue(boolean condition, BusinessException exception) {
        if (condition) {
            throw exception;
        }
    }

    public static void ifTrue(boolean condition, BusinessEntityException exception) {
        if (condition) {
            throw exception;
        }
    }

    public static void ifFalse(boolean condition, String code) {
        if (!condition) {
            throw BusinessException.exception(code);
        }
    }

    public static void ifFalse(boolean condition, BusinessException exception) {
        if (!condition) {
            throw exception;
        }
    }

    public static void ifFalse(boolean condition, BusinessEntityException exception) {
        if (!condition) {
            throw exception;
        }
    }


    public static void ifHasText(String str, String code) {
        if (StringUtils.hasText(str)) {
            throw BusinessException.exception(code);
        }
    }

    public static void ifHasNoText(String str, String code) {
        if (!StringUtils.hasText(str)) {
            throw BusinessException.exception(code);
        }
    }

    public static void ifHasText(String str, BusinessEntityException exception) {
        if (StringUtils.hasText(str)) {
            throw exception;
        }
    }

    public static void ifHasNoText(String str, BusinessEntityException exception) {
        if (!StringUtils.hasText(str)) {
            throw exception;
        }
    }


    public static void hasTextWithMessage(String str, String message) {
        if (StringUtils.hasText(str)) {
            throw BusinessException.exceptionWithMessage(GENERALUTIL, message);
        }
    }

    public static void hasNoTextWithMessage(String str, String message) {
        if (!StringUtils.hasText(str)) {
            throw BusinessException.exceptionWithMessage(GENERALUTIL, message);
        }
    }
}
