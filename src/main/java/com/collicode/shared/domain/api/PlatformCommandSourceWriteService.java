package com.collicode.shared.domain.api;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlatformCommandSourceWriteService<R> {

    Mono<CommandResult<R>> processCommand(CommandWrapper commandWrapper);

    Mono<CommandResult<R>> approveCommand(CommandWrapper commandWrapper);


    Flux<CommandResult<R>> processFluxCommand(CommandWrapper commandWrapper);

    Flux<CommandResult<R>> approveFluxCommand(CommandWrapper commandWrapper);

}
