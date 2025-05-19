package com.collicode.shared.domain.api;

import reactor.core.publisher.Mono;

public interface BusinessCommandRouterService {
    <T, R> Mono<CommandResult<R>> processCommand(CommandWrapper<T> commandWrapper);
}
