package com.collicode.shared.util.bag;

import org.springframework.util.Assert;

import java.math.BigDecimal;

public class Asserts {
    static String defaultCode = "400.005";

    public Asserts() {
    }

    public static void isNotNull(Object object, String code, String message) {
        try {
            Assert.notNull(object, message);
        } catch (Exception var4) {
            throw new MnetException(code, message);
        }
    }

    public static void hasText(String object, String code, String message) {
        try {
            Assert.hasText(object, message);
        } catch (Exception var4) {
            throw new MnetException(code, message);
        }
    }

    public static void isTrue(boolean value, String code, String message) {
        try {
            Assert.isTrue(value, message);
        } catch (Exception var4) {
            throw new MnetException(code, message);
        }
    }

    public static void isNotNull(Object object, String message) {
        try {
            Assert.notNull(object, message);
        } catch (Exception var3) {
            throw new MnetException(defaultCode, message);
        }
    }

    public static void hasText(String object, String message) {
        try {
            Assert.hasText(object, message);
        } catch (Exception var3) {
            throw new MnetException(defaultCode, message);
        }
    }

    public static void isNotNullOrEmpty(Object object, String message) {
        try {
            Assert.notNull(object, message);
            Assert.hasText(String.valueOf(object), message);
        } catch (Exception var3) {
            throw new MnetException(defaultCode, message);
        }
    }

    public static void isNotNullOrEmpty(Object object, String code, String message) {
        try {
            Assert.notNull(object, message);
            Assert.hasText(String.valueOf(object), message);
        } catch (Exception var4) {
            throw new MnetException(code, message);
        }
    }

    public static void isTrue(boolean value, String message) {
        try {
            Assert.isTrue(value, message);
        } catch (Exception var3) {
            throw new MnetException(defaultCode, message);
        }
    }

    public static void isBigDecimal(String value, String code, String message) {
        try {
            new BigDecimal(value);
        } catch (Exception var4) {
            throw new MnetException(code, message);
        }
    }

    public static BigDecimal isBigDecimal(String value, String message) {
        try {
            return new BigDecimal(value);
        } catch (Exception var3) {
            throw new MnetException(defaultCode, message);
        }
    }

    public static float isFloat(String value, String message) {
        try {
            return Float.valueOf(value);
        } catch (Exception var3) {
            throw new MnetException(defaultCode, message);
        }
    }

    public static float isFloat(String value, String code, String message) {
        try {
            return Float.valueOf(value);
        } catch (Exception var4) {
            throw new MnetException(code, message);
        }
    }

    public static int isInt(String value, String message) {
        try {
            return Float.valueOf(value).intValue();
        } catch (Exception var3) {
            throw new MnetException(defaultCode, message);
        }
    }

    public static int isInt(String value, String code, String message) {
        try {
            return Float.valueOf(value).intValue();
        } catch (Exception var4) {
            throw new MnetException(code, message);
        }
    }

    public static Long isLong(String value, String message) {
        try {
            return Long.valueOf(value);
        } catch (Exception var3) {
            throw new MnetException(defaultCode, message);
        }
    }

    public static Long isLong(String value, String code, String message) {
        try {
            return Long.valueOf(value);
        } catch (Exception var4) {
            throw new MnetException(code, message);
        }
    }

    public static void isNull(Object value, String message) {
        try {
            Assert.isNull(value, message);
        } catch (Exception var3) {
            throw new MnetException(defaultCode, message);
        }
    }

    public static void isNull(Object value, String code, String message) {
        try {
            Assert.isNull(value, message);
        } catch (Exception var4) {
            throw new MnetException(code, message);
        }
    }
}
