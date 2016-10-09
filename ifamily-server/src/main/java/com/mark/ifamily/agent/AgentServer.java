package com.mark.ifamily.agent;

import com.mark.ifamily.Transactions;
import com.mark.ifamily.server.Service;
import com.mark.ifamily.util.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by mark.zhu on 2016/10/8.
 */
public class AgentServer implements Service {
    private static final Logger LOGGER = LoggerFactory.getLogger(AgentServer.class);
    private String serverIp;
    private int serverPort;
    private int interval = 5000;
    private String key;
    private AtomicBoolean started = new AtomicBoolean(Boolean.FALSE);

    private volatile boolean running;

    public AgentServer(String serverIp, int serverPort, int interval, String key) {
        if (serverIp == null || serverIp.length() < 1) {
            throw new IllegalArgumentException("serverIp is null!");
        }
        if (serverPort == 0) {
            throw new IllegalArgumentException("serverPort is null!");
        }
        if (interval < 1) {
            throw new IllegalArgumentException("invalid value for interval:" + interval);
        }
        if (key == null || key.length()<1) {
            throw new IllegalArgumentException("key is null!");
        }
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.interval = interval;
        this.key = key;
    }


    @Override
    public void start() {
        String message = Transactions.PUT + Transactions.TRANSACTION_DELIMITER + key;
        if (started.compareAndSet(Boolean.FALSE, Boolean.TRUE)) {
            running = true;
            while (running) {
                Socket socket = null;
                BufferedReader in = null;
                PrintWriter out = null;
                try {
                    try {
                        socket = new Socket(serverIp, serverPort);
                        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        out = new PrintWriter(socket.getOutputStream(),true);
                        out.println(message);
                        out.flush();
                        LOGGER.info(in.readLine());
                    } catch (IOException e) {
                        LOGGER.error("",e);
                    }finally {
                        IOUtil.closeQuietly(in);
                        IOUtil.closeQuietly(out);
                    }
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    LOGGER.error("interrupted",e);
                    break;
                }
            }
        }
    }

    @Override
    public void stop() throws Exception {
        if (started.compareAndSet(Boolean.TRUE, Boolean.FALSE)) {
            running = false;
        }
    }

}
