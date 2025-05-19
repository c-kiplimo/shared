package com.collicode.shared.util;


import java.time.LocalDateTime;

import static java.time.ZoneOffset.UTC;

public class Meta {

    final String responseId;
    final String requestId;
    final String status;
    final String statusDesc;
    final String respondedAt;

    public Meta(String requestId, String responseId, String status, String statusDesc,
                String respondedAt) {
        this.responseId = responseId;
        this.requestId = requestId;
        this.status = status;
        this.statusDesc = statusDesc;
        this.respondedAt = respondedAt;
    }

    public static Meta forRequest(String requestId) {
        return new Meta(requestId, null, null, null,
                LocalDateTime.now(UTC).toString());
    }

    public static Meta success(String requestId, String responseId) {
        return new Meta(requestId, responseId, "SUCCESS", "Processed Successfully",
                LocalDateTime.now(UTC).toString());
    }

    public static Meta failure(String requestId, String responseId) {
        return new Meta(requestId, responseId, "FAILED", "Request processing failed",
                LocalDateTime.now(UTC).toString());
    }

    public String getResponseId() {
        return responseId;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public String getRespondedAt() {
        return respondedAt;
    }
}
