package com.abpgroup.managementsystem.service;

public interface MidtransService {

    String createQrisTransaction(String orderId, long amount);
    String getTransactionStatus(String orderId);
}
