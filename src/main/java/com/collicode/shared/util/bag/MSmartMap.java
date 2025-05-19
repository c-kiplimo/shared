package com.collicode.shared.util.bag;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public class MSmartMap extends AbstractSmartMap {
    private static final long serialVersionUID = 1L;

    public MSmartMap() {
        this.store = this.createMap();
    }

    public MSmartMap(Map<String, Object> map) {
        this.store = this.createMap(map);
    }

    public MSmartMap(String jsonMapString) {
        Type mapType = (new TypeToken<Map<String, Object>>() {
        }).getType();
        this.store = this.createMap((new GsonBuilder()).create().fromJson(jsonMapString, mapType));
    }

    public MSmartMap(String jsonMapString, boolean stringValue) {
        Type mapType = (new TypeToken<Map<String, String>>() {
        }).getType();
        this.store = this.createMap((new GsonBuilder()).create().fromJson(jsonMapString, mapType));
    }

    public MSmartMap(String keyPath, Object value) {
        this.store = this.createMap();
        this.put(keyPath, value);
    }

    public MSmartMap set(String keyPath, Object value) {
        this.put(keyPath, value);
        return this;
    }

    protected Map<String, Object> createMap() {
        return new LinkedHashMap();
    }

    protected Map<String, Object> createMap(Map<String, Object> map) {
        return new LinkedHashMap(map);
    }

    public void putAll(Map<? extends String, ? extends Object> m) {
    }

    public String getAsString(Object keyPath) {
        return this.toString(this.get(keyPath));
    }

    public String getAsStringDefault(Object keyPath, String defaultValue) {
        Object value = this.get(keyPath);
        return value == null ? defaultValue : this.toString(value);
    }

    public long getAsLong(Object keyPath) {
        String value = this.toString(this.get(keyPath));
        return Asserts.isLong(value, "Incorrect Integer value specified : value " + value);
    }

    public int getAsInt(Object keyPath) {
        String value = this.toString(this.get(keyPath));
        return Asserts.isInt(value, "Incorrect Integer value specified : value" + value);
    }

    public BigDecimal getAsBigDecimal(Object keyPath) {
        String value = this.toString(this.get(keyPath));
        return Asserts.isBigDecimal(value, "Incorrect big decimal speficied : value " + value);
    }

    public String getAsNumeric(Object keyPath) {

        String value = this.toString(this.get(keyPath));
        try {
            Asserts.isBigDecimal(value, "Incorrect big decimal speficied : value " + value);
            return new BigDecimal(value).toPlainString();
        } catch (Exception exception) {
            return value;

        }


    }

    public BigDecimal getAsBigDecimalOrDefault(Object keyPath, String defaultValue) {
        String value = this.toString(this.get(keyPath));
        if (value.isEmpty()) {
            value = defaultValue;
        }

        return Asserts.isBigDecimal(value, "Incorrect big decimal speficied : value " + value);
    }

    public float getAsFloat(Object keyPath) {
        String value = this.toString(this.get(keyPath));
        return Asserts.isFloat(value, "Incorrect float  speficied : value " + value);
    }

    String toString(Object result) {
        return result == null ? "" : String.valueOf(result);
    }

    public boolean getAsBoolean(Object keyPath) {
        return Boolean.valueOf(this.toString(this.get(keyPath)));
    }
}


