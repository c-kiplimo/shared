package com.collicode.shared.domain.api;


import com.collicode.shared.util.Meta;
import lombok.Data;

@Data
public class ApiRequest<T> {
    private Meta meta;
    private T payload;
}
