package com.mark.ifamily.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by mark.zhu on 2016/10/8.
 */
public class IOUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(IOUtil.class);
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                LOGGER.error("",e);
            }
        }
    }


}
