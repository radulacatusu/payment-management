package com.mine.payment.service;

import com.mine.payment.model.Account;
import com.mine.payment.model.User;
import com.mine.payment.repository.AccountRepository;
import com.mine.payment.util.AccountType;
import com.mine.payment.util.BalanceStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;


/**
 * @stefanl
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountRepository accountRepository;

    @Override
    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public long createAccount(Account account) {
        Account existingAccount = accountRepository.findByName(account.getName());
        if (existingAccount == null) {
            fillDefaultFields(account);
            return accountRepository.save(account).getId();
        }
        return -1;
    }

    private void fillDefaultFields(Account account) {
        if (Objects.isNull(account.getBalance())) {
            account.setBalance(new BigDecimal(0));
        }
        if (Objects.isNull(account.getBalanceStatus())) {
            account.setBalanceStatus(BalanceStatus.DE.name());
        }
        if (Objects.isNull(account.getBalanceTimestamp())) {
            account.setBalanceTimestamp(new Timestamp(System.currentTimeMillis()));
        }
        if (Objects.isNull(account.getAccountType())) {
            account.setAccountType(AccountType.CREDIT.name());
        }

        if (Objects.isNull(account.getDateOpened())) {
            account.setDateOpened(new Timestamp(System.currentTimeMillis()));
        }
    }
}
