package com.mark.ifamily;

import java.io.IOException;

/**
 * Created by mark.zhu on 2016/9/29.
 */
public class ShellCommand{
    private String helpFile[] = {
            "Usage: Main [--task] [-task-options]",
            "",
            "Tasks:",
            "    --start           - Creates and starts a broker using a configuration file, or a broker URI.",
            "    --stop            - Stops a running broker specified by the broker name.",
            "",
            "Task Options (Options specific to each task):",
            "    -console        - Console Mode",
            "    -h,-?,--help    - Display this help information. To display task specific help, use Main [task] -h,-?,--help"

    };

    public void execute(Options options) throws Exception {
        try {
            Command command = CommandFactory.createCommand(options.getCommand().getKey(), options.getCommand().getValue());
            if (command != null) {
                command.execute(Context.getContext(command.getConfigFile()));
            } else {
                printHelp();
            }
        } catch (IOException e) {
            throw e;
        }

    }

    private void printHelp() {
        for (String content : helpFile) {
            System.out.println(content);
        }
    }
}
