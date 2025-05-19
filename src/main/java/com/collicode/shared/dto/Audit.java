package com.collicode.shared.dto;

import java.time.LocalDateTime;

public class Audit {
    private final String dateDone;
    private final String action;
    private final TransactingUser inputter;

    private Audit(TransactingUser user, String action) {
        this.inputter = user;
        this.dateDone = LocalDateTime.now().toString();
        this.action = action;
    }

    public static Audit of(TransactingUser user, String action) {
        return new Audit(user, action);
    }

    public static Audit of(TransactingUser user) {
        return new Audit(user, "");
    }

    public String getDateDone() {
        return this.dateDone;
    }

    public TransactingUser getInputter() {
        return this.inputter;
    }

    public String getAction() {
        return this.action;
    }
}
