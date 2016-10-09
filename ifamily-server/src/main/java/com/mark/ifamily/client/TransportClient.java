package com.mark.ifamily.client;

import com.mark.ifamily.util.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

/**
 * Created by mark.zhu on 2016/10/9.
 */
public class TransportClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransportClient.class);
    private String serverIp;
    private int serverPort;

    public TransportClient(String serverIp, int serverPort) {
        if (serverIp == null || serverIp.length() < 1) {
            throw new IllegalArgumentException("serverIp is null!");
        }
        if (serverPort == 0) {
            throw new IllegalArgumentException("serverPort is null!");
        }
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    public String send(String content) throws IOException {
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            socket = new Socket(serverIp, serverPort);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);
            if (content != null) {
                out.println(content);
                out.flush();
            }
            return in.readLine();
        } catch (IOException e) {
            LOGGER.error("",e);
            throw e;
        }finally {
            IOUtil.closeQuietly(in);
            IOUtil.closeQuietly(out);
            IOUtil.closeQuietly(socket);
        }
    }

}
