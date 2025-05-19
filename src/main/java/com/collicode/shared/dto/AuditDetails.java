package com.collicode.shared.dto;

import java.util.ArrayList;
import java.util.List;

public class AuditDetails {
    private final Audit inputter;
    private final List<Audit> authorizers;

    private AuditDetails(Audit inputter) {
        this.inputter = inputter;
        this.authorizers = new ArrayList();
    }

    public static AuditDetails of(Audit inputterDetails) {
        return new AuditDetails(inputterDetails);
    }

    public static AuditDetails noAuth() {
        return of(Audit.of(TransactingUser.of("000-000-000", "ANONYMOUS")));
    }

    public AuditDetails addAuthorizer(Audit authorizerDetails) {
        this.authorizers.add(authorizerDetails);
        return this;
    }

    public Audit getInputter() {
        return this.inputter;
    }

    public List<Audit> getAuthorizers() {
        return this.authorizers;
    }
}
