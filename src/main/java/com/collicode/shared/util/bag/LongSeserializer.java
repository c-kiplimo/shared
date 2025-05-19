package com.collicode.shared.util.bag;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.math.BigDecimal;

class LongSerializer implements JsonSerializer<Long> {
    LongSerializer() {
    }

    public JsonElement serialize(final Long src, final Type typeOfSrc, final JsonSerializationContext context) {
        String value = BigDecimal.valueOf(src).toPlainString();
        return new JsonPrimitive(value);
    }
}
