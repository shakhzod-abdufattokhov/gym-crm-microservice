package com.epammicroservicetask.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Aspect
@Component
public class TransactionLogger {
    private static final Logger TRANSACTION_LOGGER = LoggerFactory.getLogger("TRANSACTION_LOG");

    @Before("execution(* com.epammicroservicetask.controller..*(..))")
    public void logTransactionStart(JoinPoint joinPoint) {
        String transaction_id = UUID.randomUUID().toString();
        TRANSACTION_LOGGER.info("Transaction started. Transaction_id: {}, Endpoint: {}, Args: {}",
                transaction_id,
                joinPoint.getSignature().toShortString(),
                joinPoint.getArgs());

        TransactionContext.setTransactionId(transaction_id);
    }

    @AfterReturning(value = "execution(* com.epammicroservicetask.controller..*(..))", returning = "response")
    public void logTransactionSuccess(JoinPoint joinPoint, Object response) {
        String transaction_id = TransactionContext.getTransactionId();
        TRANSACTION_LOGGER.info("Transaction succeeded. Transaction_id: {}, Endpoint: {}, Args: {}",
                transaction_id,
                joinPoint.getSignature().toShortString(),
                joinPoint.getArgs());
    }

    @AfterThrowing(value = "execution(* com.epammicroservicetask.controller..*(..))", throwing = "exception")
    public void logTransactionError(JoinPoint joinPoint, Exception exception) {
        String transaction_id = TransactionContext.getTransactionId();
        TRANSACTION_LOGGER.error("Transaction failed, Transaction_id: {}, Endpoint: {}, Args: {}",
                transaction_id,
                joinPoint.getSignature().toShortString(),
                joinPoint.getArgs());
    }
}
