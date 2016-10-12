package com.mark.ifamily.server;

/**
 * service层
 * Created by mark.zhu on 2016/10/8.
 */
public class IpTransactionHandler {
    private InMemoryStorage storage;

    public IpTransactionHandler() {
        storage = new InMemoryStorage();
    }

    /**
     *
     * @param key
     * @param ip
     */
    public String put(final String key,final String ip) {
        storage.put(key, ip);
        return key+":"+ip;
    }

    /**
     *
     * @param key
     * @return ip
     */
    public String get(String key) {
        return storage.get(key);
    }

    /**
     *
     * @return ip list
     */
    public String list() {
        return storage.list();
    }
    public void clear() {
        storage.clear();
    }
}
