package com.collicode.shared.service;

import com.collicode.shared.domain.api.CommandResult;
import com.collicode.shared.dto.Command;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public interface BusinessCommandHandler<T, R> {
    Function<Command<T>, Mono<CommandResult<R>>> processCommand();
}
