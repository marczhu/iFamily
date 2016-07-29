package com.mark.ifamily;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * Created by mark.zhu on 2016/7/28.
 */
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        LOGGER.info("starting...");
        try {
            new CountDownLatch(1).await();
        } catch (InterruptedException e) {
            LOGGER.error("",e);
        }
    }
}
