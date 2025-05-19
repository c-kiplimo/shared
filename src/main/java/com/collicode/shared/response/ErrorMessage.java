package com.collicode.shared.response;

public class ErrorMessage {

    String errorCode;
    String errorMessage;

    private ErrorMessage(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static ErrorMessage of(String errorCode, String errorMessage) {
        return new ErrorMessage(errorCode, errorMessage);
    }


    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
