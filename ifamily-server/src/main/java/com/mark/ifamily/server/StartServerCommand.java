package com.mark.ifamily.server;

import com.mark.ifamily.Command;
import com.mark.ifamily.Context;

/**
 * Created by mark.zhu on 2016/9/29.
 */
public class StartServerCommand implements Command {
    private static final String CONFIG_FILE = "server_config.properties";
    private TransportServer transportServer;

    @Override
    public void execute(Context context) throws Exception {
        transportServer = new TransportServer(context);
        transportServer.start();
    }

    @Override
    public String getConfigFile() {
        return CONFIG_FILE;
    }
}
