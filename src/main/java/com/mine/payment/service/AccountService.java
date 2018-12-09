package com.mine.payment.service;

import com.mine.payment.api.LoadAccountRequest;
import com.mine.payment.api.LoadAccountResponse;
import com.mine.payment.api.TransferRequest;
import com.mine.payment.exception.AccountsValidationException;
import com.mine.payment.model.Account;
import com.mine.payment.util.AccountType;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @stefanl
 */
public interface AccountService {

    /**
     * Retrieve the account with the specified id
     *
     * @param id
     * @return
     */
    Optional<Account> findById(Long id);

    /**
     * Create account
     *
     * @param account
     * @return
     */
    long createAccount(Account account);

    /**
     * @param accountType
     * @param currencyId
     * @return
     */
    Optional<Account> findByAccountTypeAndCurrencyId(AccountType accountType,
                                                     String currencyId);

    /**
     * @param request
     * @return
     * @throws AccountsValidationException
     */
    LoadAccountResponse loadAccount(long accountId, LoadAccountRequest request) throws AccountsValidationException;

    /**
     * @param debitAccount
     * @param creditAccount
     * @param amount
     */
    void updateAccounts(Account debitAccount, Account creditAccount, BigDecimal amount);

}
