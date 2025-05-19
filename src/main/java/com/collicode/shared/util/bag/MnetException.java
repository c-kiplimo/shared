package com.collicode.shared.util.bag;


public class MnetException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String code;
    private String message;

    public MnetException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public static MnetException exception(String code, String message) {
        return new MnetException(code, message);
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }


}
