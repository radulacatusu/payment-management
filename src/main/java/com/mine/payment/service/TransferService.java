package com.mine.payment.service;

import com.mine.payment.model.Account;

import java.math.BigDecimal;

/**
 * @stefanl
 */
public interface TransferService {

    /**
     * Save transfer
     *
     * @return
     */
    void saveTransfer(Account debitAccount, Account creditAccount, BigDecimal amount);
}
