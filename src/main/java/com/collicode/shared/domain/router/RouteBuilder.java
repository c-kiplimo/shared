package com.collicode.shared.domain.router;

import org.springframework.util.Assert;

import java.util.function.Function;
import java.util.function.Predicate;

public class RouteBuilder<E, R> {

    String incomingAction;
    Function<E, R> actionFunction;
    private Predicate<String> isEqualPredicate = (configuredAction) -> configuredAction.equalsIgnoreCase(
            this.incomingAction);

    protected RouteBuilder(String incomingAction) {
        Assert.notNull(incomingAction, "route Id must be provided");
        this.incomingAction = incomingAction;
    }

    public static <E, R> RouteBuilder<E, R> newRoute(String incomingAction) {
        return new RouteBuilder<>(incomingAction);
    }

    public RouteBuilder<E, R> map(QueryParam action, Function<E, R> commandHandler) {
        return this.MATCH(action, commandHandler);
    }

    public RouteBuilder<E, R> whenRoute(QueryParam action, Function<E, R> commandHandler) {
        return this.MATCH(action, commandHandler);
    }

    public RouteBuilder<E, R> whenRoute(Enum action, Function<E, R> commandHandler) {
        return this.MATCH(action.name(), commandHandler);
    }

    public RouteBuilder<E, R> map(String action, Function<E, R> commandHandler) {
        return this.MATCH(action, commandHandler);
    }

    public RouteBuilder<E, R> whenRoute(String action, Function<E, R> commandHandler) {
        return this.MATCH(action, commandHandler);
    }

    public RouteBuilder<E, R> notFound(Function<E, R> commandHandler) {
        if (this.actionFunction == null) {
            this.actionFunction = commandHandler;
        }

        return this;
    }

    public RouteBuilder<E, R> otherwise(Function<E, R> commandHandler) {
        if (this.actionFunction == null) {
            this.actionFunction = commandHandler;
        }

        return this;
    }

    private RouteBuilder<E, R> MATCH(String action, Function<E, R> commandHandler) {
        if (this.isEqualPredicate.test(action)) {
            this.actionFunction = commandHandler;
        }
        return this;
    }

    private RouteBuilder<E, R> MATCH(QueryParam param, Function<E, R> commandHandler) {
        if (this.isEqualPredicate.test(param.getCode())) {
            this.actionFunction = commandHandler;
        }
        return this;
    }

    public Function<E, R> build() {
        return this.actionFunction != null ? this.actionFunction : this::formResponse;
    }

    public R formResponse(E command) {
        return null;
    }
}
