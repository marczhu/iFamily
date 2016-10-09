package com.mark.ifamily;


import java.util.*;

public class Options {

    private LinkedHashMap<String, Object> optionsMap = new LinkedHashMap<String, Object>();

    private List<String> args = new ArrayList<String>();

    private Command command;
    private Options(String[] args) {
        if (args == null) {
            printHelp();
            System.exit(-1);
        }
        for (int i = 0; i < args.length; i++) {
            String key = args[i];
            if (key.startsWith("--")) {
                if (i + 1 >= args.length) {
                    throw new IllegalArgumentException("option needs a value: " + key);
                } else {
                    key = key.substring(2);
                    String value = args[i + 1];
                    if (!value.startsWith("-")) {
                        command = new Command(key, value);
                        i++;
                    }
                }
            } else if (key.startsWith("-")) {
                key = key.substring(1);
                int pos = key.indexOf("=");
                if (pos != -1) {
                    optionsMap.put(key.substring(0, pos), key.substring(pos + 1));
                } else {
                    optionsMap.put(key, true);
                }
            } else {
                this.args.add(key);
            }
        }
        if (command == null) {
            printHelp();
            System.exit(-1);
        }
    }
    public static Options getOptions(final String[] args) {
       return new Options(args);
    }

    public Object get(String key) {
        return optionsMap.get(key);
    }

    public Object remove(String key) {
        return optionsMap.remove(key);
    }

    public boolean hasOption(String key) {
        Object v = optionsMap.get(key);
        if (v instanceof Boolean) {
            return (Boolean) v;
        } else {
            return false;
        }
    }

    public void put(String key, Object value) {
        optionsMap.put(key, value);
    }

    public List<String> getArgs() {
        return args;
    }

    public Map<String, Object> getOptionsMap() {
        return optionsMap;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Options{");
        for (Iterator<String> it = optionsMap.keySet().iterator(); it.hasNext();){
            String key = it.next();
            sb.append(key).append("=").append(optionsMap.get(key));
            if (it.hasNext()) {
                sb.append(",");
            }
        }
        sb.append('}').append("command=").append(command);

        return sb.toString();
    }

    private void printHelp(){
        String helpFile[] = {
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
        for (String content : helpFile) {
            System.out.println(content);
        }
    }

    class Command{
        String key;
        String value;

        public Command(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("Command{");
            sb.append("key='").append(key).append('\'');
            sb.append(", value='").append(value).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
    public static void main(String[] args) {
        String command = "--start server -console -ins=instance_name -server.port=10999";
        Options options = Options.getOptions(command.split(" "));
        System.out.println(options.toString());
    }
}
