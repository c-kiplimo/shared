package com.collicode.shared.service;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface CommandType {

    /**
     * Name of the entity being worked on
     *
     * @return String
     */
    String entityName();

    /**
     * Action Being Performed
     *
     * @return String
     */
    String action();
}
