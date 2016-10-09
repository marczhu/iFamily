package com.mark.ifamily.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DAO层
 * Created by mark.zhu on 2016/10/8.
 */
public class InMemoryStorage {
    private Map<String, String> map = new ConcurrentHashMap<String, String>();

    /**
     *
     * @param key
     * @param value
     */
    public void put(final String key,final String value) {
        map.put(key, value);
    }

    /**
     *
     * @param key
     * @return ip
     */
    public String get(String key) {
        return map.get(key);
    }

    /**
     *
     * @return ip list
     */
    public String list() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey()).append(":").append(entry.getValue()).append(";");
        }
        return sb.toString();
    }

    public void clear() {
        map.clear();
    }
}
