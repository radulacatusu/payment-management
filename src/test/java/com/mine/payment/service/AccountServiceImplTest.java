package com.mine.payment.service;

import com.mine.payment.api.LoadAccountResponse;
import com.mine.payment.model.Account;
import com.mine.payment.repository.AccountRepository;
import com.mine.payment.util.AccountType;
import com.mine.payment.util.BalanceStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @stefanl
 */
@RunWith(SpringRunner.class)
public class AccountServiceImplTest {

    public static final String CURRENCY_ID = "EUR";

    @Autowired
    private AccountService accountService;
    @MockBean
    private AccountRepository accountRepository;

    @Before
    public void setUp() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Account debitAccount = newAccount(1, "Debit account", CURRENCY_ID, new BigDecimal(0.00),
                BalanceStatus.DR, now, now, AccountType.CLIENT);
        Account debitAccountTwo = newAccount(2, "Debit account two", CURRENCY_ID, new BigDecimal(0.00),
                BalanceStatus.DR, now, now, AccountType.CLIENT);
        Account ledgerAccount = newAccount(3, "Ledger account", CURRENCY_ID, new BigDecimal(100.00),
                BalanceStatus.DR, now, now, AccountType.LEDGER);
        Mockito.when(accountRepository.findById(debitAccount.getId()))
                .thenReturn(Optional.of(debitAccount));
        Mockito.when(accountRepository.findByName(debitAccount.getName()))
                .thenReturn(Optional.of(debitAccount));
        Mockito.when(accountRepository.findByAccountTypeAndCurrencyId(AccountType.LEDGER, CURRENCY_ID))
                .thenReturn(Optional.of(ledgerAccount));
        Mockito.when(accountRepository.save(debitAccountTwo))
                .thenReturn(debitAccountTwo);
    }

    @Test
    public void testFindById() {
        long id = 1;
        Optional<Account> found = accountService.findById(id);
        Assert.assertTrue(found.isPresent());
        Assert.assertEquals(found.get().getId(), id);
    }

    @Test
    public void testCreateAccount() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Account debitAccountTwo = newAccount(2, "Debit account two", CURRENCY_ID, new BigDecimal(0.00),
                BalanceStatus.DR, now, now, AccountType.CLIENT);
        long result = accountService.createAccount(debitAccountTwo);
        Assert.assertNotEquals(result, -1);
    }

    @Test
    public void testExistingAccount() {
        Account account = new Account();
        account.setName("Debit account");
        account.setCurrencyId(CURRENCY_ID);

        long result = accountService.createAccount(account);
        Assert.assertEquals(result, -1);
    }

    @Test
    public void testFindByAccountTypeAndCurrencyId() {
        Optional<Account> found =
                accountService.findByAccountTypeAndCurrencyId(AccountType.LEDGER, CURRENCY_ID);
        Assert.assertTrue(found.isPresent());
    }

    @Test
    public void testTransferBetweenAccountsCrAndDr() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Account debitedAccount = newAccount(10, "Debited account", CURRENCY_ID, new BigDecimal(30.00),
                BalanceStatus.CR, now, now, AccountType.CLIENT);

        Account creditedAccount = newAccount(11, "Credited account", CURRENCY_ID, new BigDecimal(40.00),
                BalanceStatus.DR, now, now, AccountType.CLIENT);

        Account expectedDebitedAccount = newAccount(10, "Debited account", CURRENCY_ID, new BigDecimal(20.00),
                BalanceStatus.DR, now, now, AccountType.CLIENT);

        Account expectedCreditedAccount = newAccount(11, "Credited account", CURRENCY_ID, new BigDecimal(10.00),
                BalanceStatus.CR, now, now, AccountType.CLIENT);

        Mockito.when(accountRepository.save(debitedAccount))
                .thenReturn(expectedDebitedAccount);
        Mockito.when(accountRepository.save(creditedAccount))
                .thenReturn(expectedCreditedAccount);

        accountService.updateAccounts(debitedAccount, creditedAccount, new BigDecimal(10));

        assertTrue(debitedAccount.getBalance().compareTo(new BigDecimal(20)) == 0);
        assertEquals(BalanceStatus.CR, debitedAccount.getBalanceStatus());

        assertTrue(creditedAccount.getBalance().compareTo(new BigDecimal(30)) == 0);
        assertEquals(BalanceStatus.DR, creditedAccount.getBalanceStatus());

        accountService.updateAccounts(debitedAccount, creditedAccount, new BigDecimal(20));

        assertTrue(debitedAccount.getBalance().compareTo(new BigDecimal(0)) == 0);
        assertEquals(BalanceStatus.DR, debitedAccount.getBalanceStatus());

        assertTrue(creditedAccount.getBalance().compareTo(new BigDecimal(10)) == 0);
        assertEquals(BalanceStatus.DR, creditedAccount.getBalanceStatus());

        accountService.updateAccounts(debitedAccount, creditedAccount, new BigDecimal(20));

        assertTrue(debitedAccount.getBalance().compareTo(new BigDecimal(20)) == 0);
        assertEquals(BalanceStatus.DR, debitedAccount.getBalanceStatus());

        assertTrue(creditedAccount.getBalance().compareTo(new BigDecimal(10)) == 0);
        assertEquals(BalanceStatus.CR, creditedAccount.getBalanceStatus());

    }

    @Test
    public void testTransferBetweenAccountsDrAndCr() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Account debitedAccount = newAccount(10, "Debited account", CURRENCY_ID, new BigDecimal(30.00),
                BalanceStatus.DR, now, now, AccountType.CLIENT);

        Account creditedAccount = newAccount(11, "Credited account", CURRENCY_ID, new BigDecimal(40.00),
                BalanceStatus.CR, now, now, AccountType.CLIENT);

        Account expectedDebitedAccount = newAccount(10, "Debited account", CURRENCY_ID, new BigDecimal(40.00),
                BalanceStatus.DR, now, now, AccountType.CLIENT);

        Account expectedCreditedAccount = newAccount(11, "Credited account", CURRENCY_ID, new BigDecimal(50.00),
                BalanceStatus.CR, now, now, AccountType.CLIENT);

        Mockito.when(accountRepository.save(debitedAccount))
                .thenReturn(expectedDebitedAccount);
        Mockito.when(accountRepository.save(creditedAccount))
                .thenReturn(expectedCreditedAccount);

        accountService.updateAccounts(debitedAccount, creditedAccount, new BigDecimal(10));

        assertTrue(debitedAccount.getBalance().compareTo(new BigDecimal(40)) == 0);
        assertEquals(BalanceStatus.DR, debitedAccount.getBalanceStatus());

        assertTrue(creditedAccount.getBalance().compareTo(new BigDecimal(50)) == 0);
        assertEquals(BalanceStatus.CR, creditedAccount.getBalanceStatus());
    }

    @Test
    public void testLoadAccount() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Account ledgerAccount = newAccount(10, "Debited account", CURRENCY_ID, new BigDecimal(500.00),
                BalanceStatus.DR, now, now, AccountType.CLIENT);

        Account creditedAccount = newAccount(11, "Credited account", CURRENCY_ID, new BigDecimal(40.00),
                BalanceStatus.CR, now, now, AccountType.CLIENT);

        Account expectedDebitedAccount = newAccount(10, "Debited account", CURRENCY_ID, new BigDecimal(550.00),
                BalanceStatus.DR, now, now, AccountType.CLIENT);

        Account expectedCreditedAccount = newAccount(11, "Credited account", CURRENCY_ID, new BigDecimal(90.00),
                BalanceStatus.CR, now, now, AccountType.CLIENT);

        Mockito.when(accountRepository.save(ledgerAccount))
                .thenReturn(expectedDebitedAccount);
        Mockito.when(accountRepository.save(creditedAccount))
                .thenReturn(expectedCreditedAccount);

        LoadAccountResponse response = accountService.loadAccount(ledgerAccount, creditedAccount, new BigDecimal(50));
        Assert.assertNotNull(response);
        assertTrue(response.getBalance().compareTo(expectedCreditedAccount.getBalance()) == 0);
        assertEquals(BalanceStatus.CR, response.getBalanceStatus());
        assertEquals(11, response.getAccountId());
    }

    private Account newAccount(long id,
                               String nameOnAccount,
                               String currencyId,
                               BigDecimal balance,
                               BalanceStatus balanceStatus,
                               Timestamp dateOpened,
                               Timestamp balanceTimestamp,
                               AccountType accountType) {
        return getAccount(id, nameOnAccount, currencyId, balance, balanceStatus, dateOpened, balanceTimestamp, accountType);
    }

    public static Account getAccount(long id, String nameOnAccount, String currencyId, BigDecimal balance, BalanceStatus balanceStatus, Timestamp dateOpened, Timestamp balanceTimestamp, AccountType accountType) {
        Account account = new Account();
        account.setId(id);
        account.setName(nameOnAccount);
        account.setCurrencyId(currencyId);
        account.setAccountType(accountType);
        account.setBalance(balance);
        account.setBalanceStatus(balanceStatus);
        account.setDateOpened(dateOpened);
        account.setBalanceTimestamp(balanceTimestamp);
        return account;
    }

    @TestConfiguration
    static class AccountServiceImplTestContextConfiguration {

        @Bean
        public AccountService accountService() {
            return new AccountServiceImpl();
        }
    }
}