package com.collicode.shared.domain.api;


import com.collicode.shared.dto.Trace;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;


@Builder
@Getter
public class CommandResult<R> implements Serializable {
    String commandId;
    String entityName;
    String actionName;
    String transactionId;
    R result;
    Trace trace;

    public CommandResult(CommandResultBuilder<R> commandResultBuilder) {
        this.commandId = commandResultBuilder.commandId;
        this.entityName = commandResultBuilder.entityName;
        this.actionName = commandResultBuilder.actionName;
        this.transactionId = commandResultBuilder.transactionId;
        this.result = commandResultBuilder.result;
        this.trace = commandResultBuilder.trace;
    }


    public static class CommandResultBuilder<R> {

        String commandId;
        String entityName;
        String actionName;
        String transactionId;
        R result;
        Trace trace;

        public CommandResultBuilder<R> commandId(String commandId) {
            this.commandId = commandId;
            return this;
        }

        public CommandResultBuilder<R> entityName(String entityName) {
            this.entityName = entityName;
            return this;
        }

        public CommandResultBuilder<R> actionName(String actionName) {
            this.actionName = actionName;
            return this;
        }

        public CommandResultBuilder<R> transactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public CommandResultBuilder<R> result(R result) {
            this.result = result;
            return this;
        }


        public CommandResultBuilder<R> result(Trace trace) {
            this.trace = trace;
            return this;
        }

        public CommandResult<R> build() {
            return new CommandResult<R>(this);
        }
    }


}
