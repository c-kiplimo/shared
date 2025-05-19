package com.collicode.shared.util.bag;

import com.google.gson.JsonElement;
import org.springframework.util.Assert;

import java.util.Map;

public class RequestMap extends MSmartMap {
    private static final long serialVersionUID = 1L;

    protected RequestMap() {
    }

    public RequestMap(Map<String, Object> map) {
        super(map);
    }

    public RequestMap(String jsonMapString) {
        super(jsonMapString);
    }

    private RequestMap(String jsonMapString, boolean isStringValue) {

        super(jsonMapString, isStringValue);
    }

    private RequestMap(String keyPath, Object value) {
        super(keyPath, value);
    }

    public static RequestMap newInstance() {
        return new RequestMap();
    }

    public static RequestMap from(String keyPath, String newKey, Object value) {
        return new RequestMap(keyPath, (new RequestMap()).get(newKey));
    }

    public static RequestMap fromJson(String json) {
        Assert.notNull(json, "Json cannot be available");
        return new RequestMap(json);
    }

    public static RequestMap fromJsonWithStringValue(String json) {
        Assert.notNull(json, "Json cannot be available");
        return new RequestMap(json, true);
    }

    public static RequestMap fromObject(Object jsonOb) {
        Assert.notNull(jsonOb, "Json cannot be available");
        return new RequestMap(JsonParser.jsonString(jsonOb));
    }

    public static RequestMap fromJson(String keyPath, String json) {
        Object value = (new RequestMap(json)).get(keyPath);
        Assert.notNull(value, "The path " + keyPath + " not available in json");
        return new RequestMap(JsonParser.jsonString(value));
    }

    public static RequestMap fromObject(String keyPath, Object value) {
        Object req = (new RequestMap(JsonParser.jsonString(value))).get(keyPath);
        Assert.notNull(value, "The path " + keyPath + " not available in json");
        return new RequestMap(JsonParser.jsonString(req));
    }

    public static RequestMap fromJson(String keyPath, JsonElement json) {
        Object value = (new RequestMap(JsonParser.jsonString(json))).get(keyPath);
        return new RequestMap(JsonParser.jsonString(value));
    }

    public static RequestMap fromJson(JsonElement jsonElement) {
        Assert.notNull(jsonElement, "The json element cannot be null");
        return new RequestMap(JsonParser.jsonString(jsonElement));
    }

    public static RequestMap withPathfrom(String keyPath, Object value) {
        return new RequestMap(keyPath, value);
    }

    public RequestMap set(String keyPath, Object value) {
        super.set(keyPath, value);
        return this;
    }

    public RequestMap getMap(String key) {
        return new RequestMap(JsonParser.jsonString(this.get(key)));
    }

    public String toJSONString() {
        return JsonParser.jsonString(this);
    }
}

