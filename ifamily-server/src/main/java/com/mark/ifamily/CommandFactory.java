package com.mark.ifamily;

import com.mark.ifamily.util.FactoryFinder;

import java.io.IOException;

/**
 * Created by mark.zhu on 2016/9/29.
 */
public class CommandFactory {
    private static final FactoryFinder START_COMMAND_FACTORY_FINDER = new FactoryFinder("META-INF/services/command/start");
    private static final FactoryFinder STOP_COMMAND_FACTORY_FINDER = new FactoryFinder("META-INF/services/command/stop");

    public static Command createCommand(String commandType,String type) throws IOException {
        try {
            return (Command) getFactoryFinder(commandType).newInstance(type);
        } catch (Throwable e) {
            IOException exception = new IOException("Could not load " + type + " factory:" + e, e);
            exception.initCause(e);
            throw exception;
        }
    }
    public static FactoryFinder getFactoryFinder(String commandType) {
        if (commandType == null || "".equals(commandType)) {
            throw new IllegalArgumentException("command type is null!");
        } else if (commandType.equals("start")) {
            return START_COMMAND_FACTORY_FINDER;
        } else if (commandType.equals("stop")) {
            return STOP_COMMAND_FACTORY_FINDER;
        } else {
            throw new IllegalArgumentException("command type:" + commandType + " is is not allowed!");
        }
    }

    public static void main(String[] args) {
        try {
            Command command = CommandFactory.createCommand("start", "agent");
            System.out.println(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
