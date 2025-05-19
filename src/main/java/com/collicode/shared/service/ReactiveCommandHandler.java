package com.collicode.shared.service;


import com.collicode.shared.domain.api.CommandResult;
import com.collicode.shared.dto.Command;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public interface ReactiveCommandHandler {
    <R> Function<Command, Mono<CommandResult<R>>> processCommand(Command command);
}
