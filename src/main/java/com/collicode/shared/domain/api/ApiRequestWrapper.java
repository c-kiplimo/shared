package com.collicode.shared.domain.api;


import com.collicode.shared.util.bag.RequestMap;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiRequestWrapper<T> {
    private RequestMap meta;
    private T payload;

    public ApiRequestWrapper(RequestMap meta, T payload) {
        this.meta = meta;
        this.payload = payload;
    }

    public static <R> ApiRequestWrapper<R> of(RequestMap meta, R payload) {
        return new ApiRequestWrapper<>(meta, payload);
    }

    @Override
    public String toString() {
        return "RequestWrapper{" +
                "meta=" + meta +
                ", payload=" + payload +
                '}';
    }
}
