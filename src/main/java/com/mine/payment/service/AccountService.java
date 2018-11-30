package com.mine.payment.service;

import com.mine.payment.model.Account;
import com.mine.payment.model.User;

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
}
