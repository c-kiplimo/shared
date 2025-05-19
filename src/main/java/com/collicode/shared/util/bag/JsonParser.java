package com.collicode.shared.util.bag;

import com.google.gson.GsonBuilder;

public interface JsonParser {
    static String jsonString(Object jsonOb) {
        return (new GsonBuilder()).registerTypeAdapter(Double.class, new DoubleSeserializer()).registerTypeAdapter(Long.class, new LongSerializer()).create().toJson(jsonOb);
    }
}
