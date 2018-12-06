package com.mine.payment.repository;

import com.mine.payment.model.Account;
import com.mine.payment.model.User;
import com.mine.payment.util.AccountType;
import com.mine.payment.util.BalanceStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;

import static com.mine.payment.service.AccountServiceImplTest.getAccount;
import static org.junit.Assert.*;

/**
 * @stefanl
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryTest {
    public static final String CURRENCY_ID = "EUR";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void findByName() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Account debitedAccount = newAccount("Debited account", CURRENCY_ID, new BigDecimal(30.00),
                BalanceStatus.CR, now, now, AccountType.CLIENT);

        entityManager.persist(debitedAccount);
        entityManager.flush();

        Optional<Account> found = accountRepository.findByName(debitedAccount.getName());
        Assert.assertTrue(found.isPresent());
        Assert.assertEquals(found.get().getName(), debitedAccount.getName());
    }

    @Test
    public void findByAccountTypeAndCurrencyId() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Account debitedAccount = newAccount( "Ledger account", "USD", new BigDecimal(70.00),
                BalanceStatus.DR, now, now, AccountType.LEDGER);

        entityManager.persist(debitedAccount);
        entityManager.flush();

        Optional<Account> found = accountRepository.findByAccountTypeAndCurrencyId(AccountType.LEDGER, "USD");
        Assert.assertTrue(found.isPresent());
        Assert.assertEquals(found.get().getName(), debitedAccount.getName());
    }

    private Account newAccount(String nameOnAccount,
                               String currencyId,
                               BigDecimal balance,
                               BalanceStatus balanceStatus,
                               Timestamp dateOpened,
                               Timestamp balanceTimestamp,
                               AccountType accountType) {
        Account account = new Account();
        account.setName(nameOnAccount);
        account.setCurrencyId(currencyId);
        account.setAccountType(accountType);
        account.setBalance(balance);
        account.setBalanceStatus(balanceStatus);
        account.setDateOpened(dateOpened);
        account.setBalanceTimestamp(balanceTimestamp);
        return account;
    }
}