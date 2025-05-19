package com.collicode.shared.exception;


public class Exceptions {

    public static void fireExceptions(String message) {
        throw new SystemException(message);
    }

    public static void fireExceptionsIfTrue(boolean fire, String message) {
        if (fire) {
            throw new SystemException(message);
        }
    }

    public static void fireExceptionsIfFalse(boolean fire, String message) {

        if (!fire) {
            throw new SystemException(message);
        }
    }

    public static void fireExceptionsIfTrue(boolean fire, BusinessException exception) {
        if (fire) {
            throw exception;
        }
    }

    public static void fireExceptionsIfFalse(boolean fire, BusinessException exception) {
        if (!fire) {
            throw exception;
        }
    }


}
