package com.collicode.shared.domain.api;


import com.collicode.shared.dto.CInfo;
import com.collicode.shared.dto.Command;
import com.collicode.shared.dto.Trace;
import com.collicode.shared.util.Meta;
import com.collicode.shared.util.TransactionIdGenerator;
import lombok.Getter;
import org.springframework.util.Assert;

import java.time.LocalDateTime;


@Getter
public class CommandWrapper<T> {

    String entityName;
    String actionName;
    String transactionId;
    Meta meta;
    T payload;
    String href;
    String permission;
    AuditInfo auditInfo;

    CommandWrapper(CommandWrapperBuilder<T> builder) {
        this.entityName = builder.entityName;
        this.actionName = builder.actionName;
        this.meta = builder.meta;
        this.payload = builder.payload;
        this.href = builder.href;
        this.permission = builder.entityName + ":" + builder.actionName;
        this.auditInfo = builder.auditInfo;
        validate();
    }

    public static <T> CommandWrapperBuilder<T> builder() {
        return new CommandWrapperBuilder<T>();
    }

    public void validate() {
        Assert.notNull(entityName, "Entity Name must be provided");
        Assert.notNull(actionName, "Action Name must be provided");
    }

    public String getRouteId() {
        return entityName + "_" + actionName;
    }

    public String getTransactionId() {
        return TransactionIdGenerator.INSTANCE.generateTransactionId();
    }

    public Command<T> getCommand() {
        //.withAuditDetails(AuditDetails.of(Audit.of()))
        return Command
                .<T>builder()
                .commandInfo(CInfo.of(entityName, actionName))
                .traceDetails(Trace.builder()
                        .channel(auditInfo.getSource())
                        .lang(auditInfo.getLang())
                        .receivedAt(LocalDateTime.now())
                        .requestId(auditInfo.getRequestId())
                        .transactionId(transactionId)
                        .tenantCode(auditInfo.getTenantCode())
                        .permission(permission)
                        .href(href)
                        .build())
                .withMeta(this.meta)
                .withPayload(this.payload)
                .build();
    }


    public static class CommandWrapperBuilder<T> {

        String entityName;
        String actionName;
        String transactionId;
        Meta meta;
        T payload;
        String href;
        String permission;
        String tenantId;
        String customerId;
        AuditInfo auditInfo;


        public CommandWrapperBuilder<T> entityName(String entityName) {
            this.entityName = entityName;
            return this;
        }

        public CommandWrapperBuilder<T> actionName(String actionName) {
            this.actionName = actionName;
            return this;
        }


        private CommandWrapperBuilder<T> transactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public CommandWrapperBuilder<T> payload(T payload) {
            this.payload = payload;
            return this;
        }

        public CommandWrapperBuilder<T> withOriginalApiRequest(ApiRequest<T> apiRequest) {
            meta = apiRequest.getMeta();
            payload = apiRequest.getPayload();
            return this;
        }

        public CommandWrapperBuilder<T> meta(Meta meta) {
            this.meta = meta;
            return this;
        }


        public CommandWrapperBuilder<T> href(String href) {
            this.href = href;
            return this;
        }

        public CommandWrapperBuilder<T> permission(String permission) {
            this.permission = permission;
            return this;
        }

        public CommandWrapperBuilder<T> customerId(String customerId) {
            this.customerId = customerId;
            return this;
        }


        public CommandWrapperBuilder<T> auditInfo(AuditInfo auditInfo) {
            this.auditInfo = auditInfo;
            return this;
        }

        public CommandWrapper<T> build() {
            return new CommandWrapper<>(this);
        }
    }
}
