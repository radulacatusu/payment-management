package com.mine.payment.repository;

import com.mine.payment.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @stefanl
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * @param name
     * @return
     */
    Account findByName(String name);
}
