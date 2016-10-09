package com.mark.ifamily.server;

/**
 * 命令格式异常
* Created by mark.zhu on 2016/10/8.
*/
class InvalidTransactionException extends Exception{

    public InvalidTransactionException(String message) {
        super(message);
    }

    public InvalidTransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTransactionException(Throwable cause) {
        super(cause);
    }

    public InvalidTransactionException() {
    }
}
