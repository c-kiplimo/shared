package com.collicode.shared.service;


import com.collicode.shared.exception.Exceptions;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CommandHandlerProvider implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(CommandHandlerProvider.class);
    private ApplicationContext applicationContext;
    private Map<String, String> registeredHandlers;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.initializeHandlerRegistry();
    }

    public <T, R> BusinessCommandHandler<T, R> commandHandler(final String entityName, final String action) {

        try {
            Assert.hasText(entityName, "Property  must be provided!");
            String key = entityName + "_" + action;
            Exceptions.fireExceptionsIfTrue(Objects.isNull(this.registeredHandlers), "Handler Register Not found");
            String handler = this.registeredHandlers.get(key);

            Assert.notNull(handler, "Command Handler [" + entityName + "] not found for");
            Object commandHandler = this.applicationContext.getBean(handler);
            return (BusinessCommandHandler) commandHandler;
        } catch (BeansException exception) {
            String key = entityName + "_" + action;
            Exceptions.fireExceptions("Command Handler [" + key + "] not found for");
        }
        return null;

    }

    public ReactiveCommandHandler getReactiveHandler(final String entityName, final String action) {
        Assert.hasText(entityName, "Property  must be provided!");
        String key = entityName + "_" + action;
        Object handler = this.registeredHandlers.get(key);

        Assert.notNull(handler, "Command Handler [" + entityName + "] not found for");
        return (ReactiveCommandHandler) this.applicationContext.getBean(this.registeredHandlers.get(entityName));
    }

    private void initializeHandlerRegistry() {

        if (this.registeredHandlers == null) {
            this.registeredHandlers = new ConcurrentHashMap<>();
            final String[] commandHandlerBeans = this.applicationContext.getBeanNamesForAnnotation(CommandType.class);
            if (ArrayUtils.isNotEmpty(commandHandlerBeans)) {
                for (final String commandHandlerName : commandHandlerBeans) {


                    try {
                        final CommandType commandType = this.applicationContext.findAnnotationOnBean(commandHandlerName,
                                CommandType.class);
                        logger.info("Registered action handler '" + commandHandlerName + " ... with key "
                                + commandType.entityName());
                        String key = commandType.entityName() + "_" + commandType.action();
                        this.registeredHandlers.put(key, commandHandlerName);
                    } catch (final Throwable th) {
                        logger.error("Unable to register command handler '" + commandHandlerName + "'!", th);
                    }
                }
            }
        }
    }
}

