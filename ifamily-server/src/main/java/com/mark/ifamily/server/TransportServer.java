package com.mark.ifamily.server;

import com.mark.ifamily.Options;
import org.apache.log4j.net.SocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by mark.zhu on 2016/9/30.
 */
public class TransportServer implements Service {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransportServer.class);
    private Options options;
    public static final int DEFAULT_PORT = 1088;
    private transient Thread shutdownHook;
    private final AtomicBoolean started = new AtomicBoolean(false);
    private final AtomicBoolean stopped = new AtomicBoolean(false);
    private final AtomicBoolean stopping = new AtomicBoolean(false);
    private final CountDownLatch stoppedLatch = new CountDownLatch(1);
    private final CountDownLatch startedLatch = new CountDownLatch(1);
    private final List<Runnable> shutdownHooks = new ArrayList<Runnable>();
    private Server server;
    private Date startDate;

    public TransportServer(Options options) {
        this.options = options;
    }

    public void start() throws Exception {
        if (stopped.get() || !started.compareAndSet(false, true)) {
            // lets just ignore redundant start() calls
            // as its way too easy to not be completely sure if start() has been
            // called or not with the gazillion of different configuration
            // mechanisms
            // throw new IllegalStateException("Already started.");
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
        addShutdownHook();
        server = new Server();
        server.start();
        startedLatch.countDown();
    }

    protected void containerShutdown() {
        try {
            stop();
        } catch (IOException e) {
            Throwable linkedException = e.getCause();
            if (linkedException != null) {
                LOGGER.error("Failed to shut down: " + e + ". Reason: " + linkedException, linkedException);
            } else {
                LOGGER.error("Failed to shut down: " + e, e);
            }

        } catch (Exception e) {
            LOGGER.error("Failed to shut down: " + e, e);
        }
    }

    public void stop() throws Exception {
        if (!stopping.compareAndSet(false, true)) {
            LOGGER.trace("Broker already stopping/stopped");
            return;
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("TransportServer is shutting down");
        }
        removeShutdownHook();

        try {
            server.stop();
        } finally {
            stopped.set(true);
            stoppedLatch.countDown();
        }

        synchronized (shutdownHooks) {
            for (Runnable hook : shutdownHooks) {
                try {
                    hook.run();
                } catch (Throwable e) {
                    LOGGER.error("Could not stop.Reason: ", e);
                }
            }
        }

        // and clear start date
        startDate = null;
    }

    protected void addShutdownHook() {
        shutdownHook = new Thread("ActiveMQ ShutdownHook") {
            @Override
            public void run() {
                containerShutdown();
            }
        };
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    protected void removeShutdownHook() {
        if (shutdownHook != null) {
            try {
                Runtime.getRuntime().removeShutdownHook(shutdownHook);
            } catch (Exception e) {
                LOGGER.debug("Caught exception, must be shutting down: " + e);
            }
        }
    }

    class Server {
        private Map<String, String> map = new ConcurrentHashMap<String, String>();
        private void start() {
            int port = DEFAULT_PORT;
            if (options.get("port") != null) {
                port = (Integer)options.get("port");
            }
            ServerSocket server = null;
            try {
                server = new ServerSocket(port);
                Socket socket;
                BufferedReader in;
                PrintWriter out;
                while (true) {
                    socket = server.accept();
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new PrintWriter(socket.getOutputStream(), true);
                    String line = in.readLine();
                    if (line == null) {
                        Thread.sleep(100);
                        continue;
                    }
                    InetAddress address = socket.getInetAddress();
                    String ip = address.getHostAddress();
                    map.put(line, ip);
                    out.println(ip);
                    out.flush();
                    Thread.sleep(1000);
                    out.close();
                    in.close();
                }
            } catch (Exception e) {
                LOGGER.error("",e);
            }finally {
                if (server != null) {
                    try {
                        server.close();
                    } catch (IOException e) {
                    }
                }
            }
        }

        private void stop() {

        }
    }
}
