package com.collicode.shared.domain.api;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public abstract class PlatformCommandCommandRouterService<T>
        implements PlatformCommandSourceWriteService<T> {

    // Process command using Mono routes
    public Mono<CommandResult<T>> processCommand(CommandWrapper commandWrapper) {

        Function<CommandWrapper, Mono<CommandResult<T>>> monoRoute = loadRoutes(commandWrapper);
        return monoRoute != null ? monoRoute.apply(commandWrapper) : Mono.empty();
    }

    // Approve command using Mono routes
    public Mono<CommandResult<T>> approveCommand(CommandWrapper commandWrapper) {
        Function<CommandWrapper, Mono<CommandResult<T>>> monoRoute = loadRoutes(commandWrapper);
        return monoRoute != null ? monoRoute.apply(commandWrapper) : Mono.empty();
    }

    // Abstract method for loading Mono routes
    protected abstract Function<CommandWrapper, Mono<CommandResult<T>>> loadRoutes(CommandWrapper commandWrapper);

    // Process command using Flux routes
    public Flux<CommandResult<T>> processFluxCommand(CommandWrapper commandWrapper) {
        Function<CommandWrapper, Flux<CommandResult<T>>> fluxRoute = loadFluxRoutes(commandWrapper);
        return fluxRoute != null ? fluxRoute.apply(commandWrapper) : Flux.empty();
    }

    // Approve command using Flux routes
    public Flux<CommandResult<T>> approveFluxCommand(CommandWrapper commandWrapper) {
        Function<CommandWrapper, Flux<CommandResult<T>>> fluxRoute = loadFluxRoutes(commandWrapper);
        return fluxRoute != null ? fluxRoute.apply(commandWrapper) : Flux.empty();
    }

    // Abstract method for loading Flux routes
    protected abstract Function<CommandWrapper, Flux<CommandResult<T>>> loadFluxRoutes(CommandWrapper commandWrapper);
}
