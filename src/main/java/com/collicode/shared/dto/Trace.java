package com.collicode.shared.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class Trace {
    private String userId;
    private String userName;
    private String tenantCode;
    private String requestId;
    private String transactionId;
    private String lang;
    private String channel;
    private String permission;
    private String href;

    private LocalDateTime receivedAt;
}
