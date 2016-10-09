package com.mark.ifamily.server;

import java.util.HashMap;
import java.util.Map;

/**
* Created by mark.zhu on 2016/10/8.
*/
class Request {
    private Map<String, String> params = new HashMap<String, String>();

    public String getParameter(String key) {
        return params.get(key);
    }

    public void addParameter(String key,String value) {
        params.put(key, value);
    }
}
