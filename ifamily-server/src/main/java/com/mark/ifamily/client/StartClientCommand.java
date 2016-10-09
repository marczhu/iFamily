package com.mark.ifamily.client;

import com.mark.ifamily.Command;
import com.mark.ifamily.Context;
import com.mark.ifamily.Options;
import com.mark.ifamily.Transactions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mark.zhu on 2016/9/29.
 */
public class StartClientCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartClientCommand.class);
    private static final String CONFIG_FILE = "client_config.properties";
    @Override
    public void execute(Context context) throws Exception {
        TransportClient transportClient = new TransportClient(context.getString("remote.server.ip"),context.getInt("remote.server.port"));
        String content = Transactions.GET + Transactions.TRANSACTION_DELIMITER + context.getString("key");
        LOGGER.info("REQUEST:" + content);
        String result = transportClient.send(content);
        LOGGER.info("RESPONSE:" + result);
    }

    @Override
    public String getConfigFile() {
        return CONFIG_FILE;
    }
}
