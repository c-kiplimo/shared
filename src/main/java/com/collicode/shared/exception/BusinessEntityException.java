package com.collicode.shared.exception;

import lombok.Getter;

import static com.collicode.shared.exception.ExceptionCode.*;


/**
 * This error is thrown whenever we have validation issue at business entity level
 */
@Getter
public class BusinessEntityException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final String entityName;
    private final String code;
    private final String key;

    private BusinessEntityException(String entityName, String code, String key) {
        super(entityName + ", KEY=" + key + ", CODE=" + code);
        this.entityName = entityName;
        this.code = code;
        this.key = key;
    }

    public static BusinessEntityException missingKeyException(String entityName, String key) {
        return new BusinessEntityException(entityName, MISSINGKEY, key);
    }

    public static BusinessEntityException entityNotFoundException(String entityName) {
        return new BusinessEntityException(entityName, ENTITYNOTFOUND, "");
    }

    public static BusinessEntityException invalidEntryException(String entityName, String key) {
        return new BusinessEntityException(entityName, INVALIDENTRY, key);
    }


}
