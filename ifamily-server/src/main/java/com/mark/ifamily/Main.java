package com.mark.ifamily;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 1.server: agent注册(tcp)、保存agent ip(内存中保存)、提供agent查询服务(tcp\http)、linux开机自动启动
 * 2.agent：目标agent
 * 3.client:查询agent外网IP地址
 * Created by mark.zhu on 2016/7/28.
 */
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        try {
            String command = "--start server";
            Options options = Options.getOptions(command.split(" "));
            ShellCommand shellCommand = new ShellCommand();
            shellCommand.execute(options);
        } catch (Exception e) {
            LOGGER.error("",e);
            System.exit(1);
        }
    }
}
