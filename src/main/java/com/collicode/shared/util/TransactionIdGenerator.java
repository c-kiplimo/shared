package com.collicode.shared.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public enum TransactionIdGenerator {
    INSTANCE;

    public String generateTransactionId(String application) {
        SimpleDateFormat format = new SimpleDateFormat("YYDD");
        String date = format.format(new Date());
        String shortId = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
        return application + date + shortId;
    }

    public String generateTransactionId() {
        SimpleDateFormat format = new SimpleDateFormat("YYDD");
        String date = format.format(new Date());
        String shortId = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
        return date + shortId;
    }

}
