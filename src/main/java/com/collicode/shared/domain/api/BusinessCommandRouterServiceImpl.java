package com.collicode.shared.domain.api;


import com.collicode.shared.dto.Command;
import com.collicode.shared.service.BusinessCommandHandler;
import com.collicode.shared.service.CommandHandlerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Service
public class BusinessCommandRouterServiceImpl implements BusinessCommandRouterService {

    @Autowired
    CommandHandlerProvider commandHandlerProvider;


    @Override
    public <T, R> Mono<CommandResult<R>> processCommand(CommandWrapper<T> commandWrapper) {
        //add authentication validation if required
        BusinessCommandHandler<T, R> businessCommandHandler = commandHandlerProvider
                .commandHandler(commandWrapper.getEntityName(), commandWrapper.getActionName());
        Function<Command<T>, Mono<CommandResult<R>>> process = businessCommandHandler.processCommand();
        return process.apply(commandWrapper.getCommand());
    }
}
