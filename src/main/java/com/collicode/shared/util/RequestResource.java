package com.collicode.shared.util;


import com.collicode.shared.domain.valueobject.RequestMeta;
import lombok.Data;


@Data
public class RequestResource<T> {

    RequestMeta meta;
    T payload;


}
