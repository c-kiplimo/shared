package com.collicode.shared.util;


import com.collicode.shared.domain.api.AuditInfo;

public class APIResponseUtil {

    public static APIResponse successApiResponse(AuditInfo auditInfo, Object apiDto) {
        return new APIResponse(Meta.success(auditInfo.getRequestId(), auditInfo.getTransactionId()),
                apiDto);
    }

    public static APIResponse successApiListResponse(AuditInfo auditInfo, Object apiDto) {
        return new APIResponse(Meta.success(auditInfo.getRequestId(), auditInfo.getTransactionId()),
                apiDto);
    }

    public static APIResponse failedApiResponse(AuditInfo auditInfo, Object apiDto) {
        return new APIResponse(Meta.failure(auditInfo.getRequestId(), auditInfo.getTransactionId()),
                apiDto);
    }

    public static APIResponse failedApiListResponse(AuditInfo auditInfo, Object apiDto,
                                                    String responseId) {
        return new APIResponse(Meta.failure(auditInfo.getRequestId(), responseId),
                apiDto);
    }


}
