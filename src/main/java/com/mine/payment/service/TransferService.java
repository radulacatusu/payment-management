package com.mine.payment.service;

import com.mine.payment.api.TransferRequest;
import com.mine.payment.exception.AccountsValidationException;

/**
 * @stefanl
 */
public interface TransferService {

    /**
     * Save transfer
     *
     * @return
     */
    void transferAmount(TransferRequest request) throws AccountsValidationException;
}
