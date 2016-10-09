package com.mark.ifamily.server;

import com.mark.ifamily.Context;
import com.mark.ifamily.util.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by mark.zhu on 2016/9/30.
 */
public class TransportServer implements Service {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransportServer.class);
    private Context context;
    public static final int DEFAULT_PORT = 7074;
    private final AtomicBoolean started = new AtomicBoolean(false);
    private final AtomicBoolean stopped = new AtomicBoolean(false);
    private final AtomicBoolean stopping = new AtomicBoolean(false);
    private final CountDownLatch stoppedLatch = new CountDownLatch(1);
    private final CountDownLatch startedLatch = new CountDownLatch(1);
    private Server server;
    private Date startDate;

    public TransportServer(Context context) {
        this.context = context;
    }

    public void start() throws Exception {
        if (stopped.get() || !started.compareAndSet(false, true)) {
            return;
        }
        stopping.set(false);
        startDate = new Date();
        try {
            startServer();
        } catch (Exception e) {
            LOGGER.error("Failed to start TransportServer. Reason: " + e, e);
            try {
                if (!stopped.get()) {
                    stop();
                }
            } catch (Exception ex) {
                LOGGER.warn("Failed to stop TransportServer after failure in start. This exception will be ignored.", ex);
            }
            throw e;
        }
    }

    private void startServer() {
        server = new Server();
        server.start();
        startedLatch.countDown();
    }



    public void stop() throws Exception {
        if (!stopping.compareAndSet(false, true)) {
            LOGGER.trace("Broker already stopping/stopped");
            return;
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("TransportServer is shutting down");
        }

        try {
            server.stop();
        } finally {
            stopped.set(true);
            stoppedLatch.countDown();
        }

        // and clear start date
        startDate = null;
    }



    class Server {
        private SocketHandler handler;

        public Server() {
            handler = SocketHandler.getSocketHandler();
        }

        private void start() {
            int port = DEFAULT_PORT;
            if (context.getInt("server.port") != 0) {
                port = context.getInt("server.port");
            }
            ServerSocket server = null;
            try {
                server = new ServerSocket(port);
                Socket socket;
                while (true) {
                    try {
                        socket = server.accept();
                        socket.setSoTimeout(10000);
                        handler.handleSocket(socket);
                    } catch (IOException e) {
                        LOGGER.error("this will be never happened!",e);
                    } catch (Exception e) {
                        LOGGER.error("this will be never happened!",e);
                    }
                }
            } catch (IOException e) {
                LOGGER.error("", e);
            } catch (Exception e) {
                LOGGER.error("", e);
            } finally {
                IOUtil.closeQuietly(server);
            }
        }

        private void stop() {

        }

    }
}
