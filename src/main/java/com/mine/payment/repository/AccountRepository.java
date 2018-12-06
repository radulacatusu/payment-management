package com.mine.payment.repository;

import com.mine.payment.model.Account;
import com.mine.payment.util.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @stefanl
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * @param name
     * @return
     */
    Optional<Account> findByName(String name);

    /**
     * @param accountType
     * @param currencyId
     * @return
     */
    Optional<Account> findByAccountTypeAndCurrencyId(AccountType accountType, String currencyId);
}
