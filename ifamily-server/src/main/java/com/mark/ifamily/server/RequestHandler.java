package com.mark.ifamily.server;

import com.mark.ifamily.Transactions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 处理请求
 * Created by mark.zhu on 2016/10/8.
 */
public class RequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestHandler.class);
    private IpTransactionHandler handler;

    public RequestHandler() {
        handler = new IpTransactionHandler();
    }

    /**
     * @param request
     * @return
     * @throws Exception
     */
    public Response handleRequest(Request request) throws Exception {
        if (request == null) {
            return null;
        }
        String param = request.getParameter("param");
        if (param == null) {
            throw new InvalidTransactionException("param is null!");
        }
        LOGGER.info("REQUEST:" + param);
        Response response = new Response();

        String transactionType = Transactions.getTransaction(param);
        if (Transactions.PUT.equalsIgnoreCase(transactionType)) {
            String key = param.substring(param.indexOf(Transactions.TRANSACTION_DELIMITER) + 1);
            checkKey(key);
            handler.put(key, request.getParameter("ip"));
        } else if (Transactions.GET.equalsIgnoreCase(transactionType)) {
            String key = param.substring(param.indexOf(Transactions.TRANSACTION_DELIMITER) + 1);
            checkKey(key);
            response.setContent(handler.get(key));
        } else if (Transactions.LIST.equalsIgnoreCase(transactionType)) {
            response.setContent(handler.list());
        } else if (Transactions.PRINT.equalsIgnoreCase(transactionType)) {
            response.setContent(param);
        } else if (Transactions.CLEAR.equalsIgnoreCase(transactionType)) {
            handler.clear();
        } else {
            throw new InvalidTransactionException("unknown transaction!");
        }
        LOGGER.info("RESPONSE:" + response.getContent());
        return response;
    }

    private void checkKey(String key) throws BusinessException {
        if (key == null || key.length() == 0) {
            throw new BusinessException("key is null!");
        }
    }
}
