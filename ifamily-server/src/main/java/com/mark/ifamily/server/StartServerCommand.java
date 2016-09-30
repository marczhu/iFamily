package com.mark.ifamily.server;

import com.mark.ifamily.Command;
import com.mark.ifamily.Options;
import org.apache.activemq.broker.BrokerService;

import java.net.URI;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by mark.zhu on 2016/9/29.
 */
public class StartServerCommand implements Command {
    private TransportServer transportServer;
    @Override
    public void execute(Options options) throws Exception {
        transportServer = new TransportServer(options);
        transportServer.start();
    }

}
