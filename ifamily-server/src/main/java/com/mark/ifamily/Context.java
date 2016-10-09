package com.mark.ifamily;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 应用上下文,配置参数
 * Created by mark.zhu on 2016/10/8.
 */
public class Context {
    private Properties properties;

    private Context(String configFile) throws IOException {
        properties = loadProperties(configFile);
    }

    public static Context getContext(String configFile) throws IOException {
        return new Context(configFile);
    }
    public String getString(String key){
        return (String)properties.get(key);
    }
    public int getInt(String key){
        return new Integer((String)properties.get(key)).intValue();
    }

    static public Properties loadProperties(String uri) throws IOException {
        // lets try the thread context class loader first
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = Context.class.getClassLoader();
        }
        InputStream in = classLoader.getResourceAsStream(uri);
        if (in == null) {
            throw new IOException("Could not find factory class for resource: " + uri);
        }

        // lets load the file
        BufferedInputStream reader = null;
        try {
            reader = new BufferedInputStream(in);
            Properties properties = new Properties();
            properties.load(reader);
            return properties;
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
            }
        }
    }

    public static void main(String[] args) {
        try {
            Context context = Context.getContext("agent_config.properties");
            System.out.println(context.getString("remote.server.ip"));
            System.out.println(context.getInt("remote.server.port"));
            System.out.println(context.getString("key"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

