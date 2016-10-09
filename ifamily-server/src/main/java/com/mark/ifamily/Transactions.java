package com.mark.ifamily;

/**
 * Created by mark.zhu on 2016/10/9.
 */
public class Transactions {
    public static String PUT = "PUT";
    public static String GET = "GET";
    public static String LIST= "LIST";
    public static String PRINT= "PRINT";
    public static String CLEAR= "CLEAR";
    public static String TRANSACTION_DELIMITER = ":";

    public static String getTransaction(String content){
        int pos = content.indexOf(TRANSACTION_DELIMITER);
        if (pos != -1) {
            return content.substring(0, pos);
        }else{
            return content;
        }
    }
}
