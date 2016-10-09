package com.mark.ifamily.agent;

import com.mark.ifamily.Command;
import com.mark.ifamily.Context;

/**
 * Created by mark.zhu on 2016/9/29.
 */
public class StartAgentCommand implements Command {
    private static final String CONFIG_FILE = "agent_config.properties";
    private AgentServer agentServer;
    @Override
    public void execute(Context context) throws Exception {
        agentServer = new AgentServer(context.getString("remote.server.ip"),context.getInt("remote.server.port"),context.getInt("heartbeat.interval"),context.getString("key"));
        agentServer.start();
    }

    @Override
    public String getConfigFile() {
        return CONFIG_FILE;
    }
}
