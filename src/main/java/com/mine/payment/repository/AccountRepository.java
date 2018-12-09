package com.mine.payment.repository;

import com.mine.payment.model.Account;
import com.mine.payment.util.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Optional;

/**
 * @stefanl
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Account> findById(Long id);

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
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Account> findByAccountTypeAndCurrencyId(AccountType accountType, String currencyId);
}
