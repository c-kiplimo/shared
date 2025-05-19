package com.collicode.shared.domain.api;


import com.collicode.shared.util.TransactionIdGenerator;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.time.LocalDateTime;

import static java.time.ZoneOffset.UTC;

public class AuditInfo {

    private final String jwtToken;
    private final LocalDateTime localDateTime;
    private final String source;
    private final String requestId;
    private final String transactionId;
    private final String lang;
    private final String tenantCode;

    private AuditInfo(String jwtToken, String requestId, String source, String lang, String tenantCode) {
        this.jwtToken = jwtToken;
        this.localDateTime = LocalDateTime.now(UTC);
        this.requestId = requestId;
        this.source = source;
        this.lang = lang;
        this.tenantCode = tenantCode;
        this.transactionId = TransactionIdGenerator.INSTANCE.generateTransactionId();
    }


    public static AuditInfo from(ServerRequest serverRequest) {
        String token = serverRequest.headers().firstHeader("Authorization");
        String requestId = serverRequest.headers().firstHeader("X-RequestId");
        String tenantCode = serverRequest.headers().firstHeader("X-TenantCode");
        String source = serverRequest.headers().firstHeader("Origin");
        String lang = serverRequest.headers().firstHeader("lang");
        return new AuditInfo(token, requestId, source, lang, tenantCode);
    }


    public String getJwtToken() {
        return jwtToken;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public String getSource() {
        return source;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getLang() {
        return lang;
    }

    public String getTenantCode() {
        return tenantCode;
    }
}
