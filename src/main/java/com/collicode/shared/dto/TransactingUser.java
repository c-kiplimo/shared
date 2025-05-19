package com.collicode.shared.dto;

public class TransactingUser {
    private final String userId;
    private final String userName;
    private String customerNumber;

    private TransactingUser(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    private TransactingUser(String userId, String userName, String customerNumber) {
        this.userId = userId;
        this.userName = userName;
        this.customerNumber = customerNumber;
    }

    public static TransactingUser of(String userId, String userName) {
        return new TransactingUser(userId, userName);
    }

    public static TransactingUser of(String userId, String userName, String customerNumber) {
        return new TransactingUser(userId, userName, customerNumber);
    }

    public String getUserId() {
        return this.userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getCustomerNumber() {
        return this.customerNumber;
    }
}
