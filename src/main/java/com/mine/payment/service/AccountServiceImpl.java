package com.mine.payment.service;

import com.mine.payment.api.LoadAccountResponse;
import com.mine.payment.model.Account;
import com.mine.payment.repository.AccountRepository;
import com.mine.payment.util.AccountType;
import com.mine.payment.util.BalanceStatus;
import org.springframework.stereotype.Service;
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
@Transactional
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountRepository accountRepository;

    @Override
    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public long createAccount(Account account) {
        Optional<Account> existingAccount = accountRepository.findByName(account.getName());
        if (!existingAccount.isPresent()) {
            fillDefaultFields(account);
            return accountRepository.save(account).getId();
        }
        return -1;
    }

    @Override
    public Optional<Account> findByAccountTypeAndCurrencyId(AccountType accountType, String currencyId) {
        return accountRepository.findByAccountTypeAndCurrencyId(accountType, currencyId);
    }


    public void updateAccounts(Account debitAccount,
                               Account creditAccount,
                               BigDecimal amount) {
        Timestamp now = new Timestamp(System.currentTimeMillis());

        debitAccount(debitAccount, amount, now);
        creditAccount(creditAccount, amount, now);

        accountRepository.save(debitAccount);
        accountRepository.save(creditAccount);
    }

    @Override
    public LoadAccountResponse loadAccount(Account debitAccount,
                                           Account creditAccount,
                                           BigDecimal amount) {
        updateAccounts(debitAccount, creditAccount, amount);

        LoadAccountResponse response = new LoadAccountResponse();
        response.setAccountId(creditAccount.getId());
        response.setBalance(creditAccount.getBalance());
        response.setBalanceStatus(creditAccount.getBalanceStatus());

        return response;
    }

    private void debitAccount(Account account,
                              BigDecimal amount,
                              Timestamp now) {
        if (BalanceStatus.DR.equals(account.getBalanceStatus())) {
            addAmount(account, amount);
        }
        if (BalanceStatus.CR.equals(account.getBalanceStatus())) {
            updateBalanceAndStatus(account, amount, BalanceStatus.DR);
        }
        account.setBalanceTimestamp(now);
    }

    private void creditAccount(Account account,
                               BigDecimal amount,
                               Timestamp now) {
        if (BalanceStatus.CR.equals(account.getBalanceStatus())) {
            addAmount(account, amount);
        }
        if (BalanceStatus.DR.equals(account.getBalanceStatus())) {
            updateBalanceAndStatus(account, amount, BalanceStatus.CR);
        }
        account.setBalanceTimestamp(now);
    }

    private void addAmount(Account account,
                           BigDecimal amount) {
        account.setBalance(account.getBalance().add(amount));
    }

    private void updateBalanceAndStatus(Account account,
                                        BigDecimal amount,
                                        BalanceStatus changedBalanceStatus) {
        BigDecimal newBalance;

        int result = account.getBalance().compareTo(amount);
        if (result > 0) {
            newBalance = account.getBalance().subtract(amount);
        } else if (result == 0) {
            newBalance = account.getBalance().subtract(amount);
            account.setBalanceStatus(BalanceStatus.DR);
        } else {
            newBalance = amount.subtract(account.getBalance());
            account.setBalanceStatus(changedBalanceStatus);
        }
        account.setBalance(newBalance);
    }


    private void fillDefaultFields(Account account) {
        if (Objects.isNull(account.getBalance())) {
            account.setBalance(new BigDecimal(0));
        }
        if (Objects.isNull(account.getBalanceStatus())) {
            account.setBalanceStatus(BalanceStatus.DR);
        }
        if (Objects.isNull(account.getBalanceTimestamp())) {
            account.setBalanceTimestamp(new Timestamp(System.currentTimeMillis()));
        }
        if (Objects.isNull(account.getAccountType())) {
            account.setAccountType(AccountType.CLIENT);
        }
        if (Objects.isNull(account.getDateOpened())) {
            account.setDateOpened(new Timestamp(System.currentTimeMillis()));
        }
    }
}
