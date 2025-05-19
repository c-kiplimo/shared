package com.collicode.shared.response;


import com.collicode.shared.exception.ErrorCode;

public interface ResponseCodeResolver {

    ErrorCode checkCode(String code);

    ErrorCode checkCode(String code, String lang);

    String keyName();

    String code();

    String message();

    String message(String lang);
}
