package com.mine.payment.service;

import com.mine.payment.api.LoadAccountRequest;
import com.mine.payment.api.LoadAccountResponse;
import com.mine.payment.exception.AccountsValidationException;
import com.mine.payment.model.Account;
import com.mine.payment.repository.AccountRepository;
import com.mine.payment.util.AccountType;
import com.mine.payment.util.BalanceStatus;
import com.mine.payment.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


/**
 * @stefanl
 */
@Service
@Transactional(isolation = Isolation.SERIALIZABLE)
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountRepository accountRepository;

    @Autowired
    private JsonUtil jsonUtil;

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


    @Override
    public LoadAccountResponse loadAccount(long accountId,
                                           LoadAccountRequest request) throws AccountsValidationException {

        Optional<Account> accountOpt = findById(accountId);
        if (!accountOpt.isPresent()) {
            throw new AccountsValidationException(HttpStatus.NOT_FOUND);
        }

        Account debitAccount = accountOpt.get();

        Optional<Account> ledgerAccount = findByAccountTypeAndCurrencyId(AccountType.LEDGER,
                request.getCurrencyId());
        if (!ledgerAccount.isPresent()) {
            throw new AccountsValidationException(HttpStatus.NOT_FOUND);
        }

        Account creditAccount = ledgerAccount.get();

        if (!debitAccount.getCurrencyId().equals(request.getCurrencyId())) {
            Map<String, String> map = new HashMap<>();
            map.put("message", "Currency mismatch between this account and ledger account.");
            throw new AccountsValidationException(jsonUtil.writeValueAsString(map), HttpStatus.PRECONDITION_FAILED);
        }

        updateAccounts(debitAccount, creditAccount, request.getAmount());

        LoadAccountResponse response = new LoadAccountResponse();
        response.setAccountId(debitAccount.getId());
        response.setBalance(debitAccount.getBalance());
        response.setBalanceStatus(debitAccount.getBalanceStatus());

        return response;
    }

    public void updateAccounts(Account debitAccount, Account creditAccount, BigDecimal amount) {
        Timestamp now = new Timestamp(System.currentTimeMillis());

        setNewBalance(debitAccount, amount, BalanceStatus.DR);
        setNewBalance(creditAccount, amount, BalanceStatus.CR);

        debitAccount.setBalanceTimestamp(now);
        creditAccount.setBalanceTimestamp(now);

        accountRepository.save(debitAccount);
        accountRepository.save(creditAccount);
    }

    private void setNewBalance(Account account,
                               BigDecimal amount,
                               BalanceStatus operationStatus) {
        BigDecimal newBalance;

        if (account.getBalanceStatus().equals(operationStatus)) {
            newBalance = account.getBalance().add(amount);
        } else if (account.getBalance().compareTo(amount) >= 0) {
            newBalance = account.getBalance().subtract(amount);
        } else {
            newBalance = amount.subtract(account.getBalance());
            account.setBalanceStatus(operationStatus);
        }

        if (newBalance.equals(BigDecimal.ZERO)) {
            account.setBalanceStatus(BalanceStatus.DR);
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
