package com.epammicroservicetask.aspect;

public class TransactionContext {
    private static final ThreadLocal<String> TRANSACTION_ID = new ThreadLocal<>();

    public static String getTransactionId() {
        return TRANSACTION_ID.get();
    }

    public static void setTransactionId(String transactionId) {
        TRANSACTION_ID.set(transactionId);
    }

    public static void clear(){
        TRANSACTION_ID.remove();
    }
}
