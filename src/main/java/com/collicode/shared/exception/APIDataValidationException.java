package com.collicode.shared.exception;


import com.collicode.shared.response.ErrorMessage;

public class APIDataValidationException extends RuntimeException {

    public APIDataValidationException(ErrorMessage errorMessage) {

    }

}
