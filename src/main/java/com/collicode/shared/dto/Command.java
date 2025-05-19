package com.collicode.shared.dto;


import com.collicode.shared.util.Meta;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.List;


@Getter
public class Command<T> {

    private final String entityName;
    private final String action;
    private final Trace trace;
    private final Meta meta;
    private final T payload;
    private final List<String> ofsExcludeFields;


    private Command(CommandBuilder<T> builder) {
        this.meta = builder.meta;
        this.payload = builder.payload;
        this.trace = builder.trace;
        this.entityName = builder.cInfo.getEntityName();
        this.action = builder.cInfo.getAction();
        this.ofsExcludeFields = builder.excludeOFSFields;
    }


    public static <T> CommandBuilder<T> builder() {
        return new CommandBuilder<>();
    }

    public boolean isLock() {
        return StringUtils.hasText(action) && action.equalsIgnoreCase("LOCK");
    }

    public static class CommandBuilder<T> {
        private Trace trace;
        private Meta meta;
        private T payload;
        private AuditDetails auditDetails;
        private CInfo cInfo;
        private List<String> excludeOFSFields;


        public CommandBuilder<T> traceDetails(Trace trace) {
            this.trace = trace;
            return this;
        }

        public CommandBuilder<T> commandInfo(CInfo cInfo) {
            this.cInfo = cInfo;
            return this;
        }

        public CommandBuilder<T> withPayload(T payload) {
            this.payload = payload;
            return this;
        }

        public CommandBuilder<T> withMeta(Meta meta) {
            this.meta = meta;
            return this;
        }

        public CommandBuilder<T> excludeOFSFields(List<String> excludeFields) {
            this.excludeOFSFields = excludeFields;
            return this;
        }

        public Command<T> build() {
            return new Command<>(this);
        }

    }

}
