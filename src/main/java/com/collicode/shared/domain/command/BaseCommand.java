package com.collicode.shared.domain.command;

public class BaseCommand {

    protected ProcessIndicator processIndicator;

    public BaseCommand() {
        this.processIndicator = ProcessIndicator.PROCESS;
    }

    public ProcessIndicator getProcessIndicator() {
        return processIndicator;
    }

    public enum ProcessIndicator {
        PROCESS, VALIDATE
    }
}
