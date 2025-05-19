package com.collicode.shared.util;

public class APIResponse {

    public final Meta meta;

    public final Object payload;

    public APIResponse(Meta header, Object payload) {
        this.meta = header;
        this.payload = payload;
    }

    public Meta getMeta() {
        return meta;
    }

    public Object getPayload() {
        return payload;
    }
}
