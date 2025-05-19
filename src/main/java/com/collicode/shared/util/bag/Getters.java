package com.collicode.shared.util.bag;

import java.math.BigDecimal;

public interface Getters {
    String getAsString(Object keyPath);

    long getAsLong(Object keyPath);

    int getAsInt(Object keyPath);

    BigDecimal getAsBigDecimal(Object keyPath);

    float getAsFloat(Object keyPath);

    boolean getAsBoolean(Object keyPath);

    BigDecimal getAsBigDecimalOrDefault(Object keyPath, String value);

    String getAsStringDefault(Object keyPath, String defaultValue);
}

