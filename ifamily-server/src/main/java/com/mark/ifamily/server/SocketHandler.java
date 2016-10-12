package com.mark.ifamily.server;

import com.mark.ifamily.util.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by mark.zhu on 2016/10/8.
 */
public class SocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketHandler.class);
    private volatile static SocketHandler socketHandler;
    private RequestHandler requestHandler;
    private SocketHandler() {
        requestHandler = new RequestHandler();
    }


    /**
     * 处理IO，不关闭socket
     * @param socket
     */
    public void handleSocket(Socket socket) {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            Request request = new Request();

            InetAddress inetAddress = socket.getInetAddress();
            InetSocketAddress remoteSocketAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
            InetAddress localAddress = socket.getLocalAddress();
            InetSocketAddress localSocketAddress = (InetSocketAddress)socket.getLocalSocketAddress();

            StringBuilder sb = new StringBuilder();
            sb.append("socket.getInetAddress().toString():"+ socket.getInetAddress().toString() );
            sb.append("socket.getRemoteSocketAddress().toString():"+socket.getRemoteSocketAddress().toString());
            sb.append("socket.getLocalSocketAddress().toString():"+ socket.getLocalSocketAddress().toString());
            sb.append("socket.getLocalSocketAddress():"+socket.getLocalSocketAddress().toString());


            request.addParameter("ip",sb.toString());
            request.addParameter("param",in.readLine());
            Response response = requestHandler.handleRequest(request);
            if (response != null && response.getContent() != null) {
                out.println(response.getContent());
                out.flush();
            }
        } catch (IOException e) {
            LOGGER.error("",e);
        } catch (Exception e) {
            LOGGER.error("", e);
        } finally {
            IOUtil.closeQuietly(out);
            IOUtil.closeQuietly(in);
        }
    }



    /**
     * singleton instance
     *
     * @return
     */
    public static SocketHandler getSocketHandler() {
        if (socketHandler == null) {
            socketHandler = new SocketHandler();
        }
        return socketHandler;
    }

}
