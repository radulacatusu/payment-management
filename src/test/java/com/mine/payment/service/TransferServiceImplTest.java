package com.mine.payment.service;

import com.mine.payment.api.TransferRequest;
import com.mine.payment.exception.AccountsValidationException;
import com.mine.payment.model.Account;
import com.mine.payment.model.Transfer;
import com.mine.payment.repository.AccountRepository;
import com.mine.payment.repository.TransferRepository;
import com.mine.payment.util.AccountType;
import com.mine.payment.util.BalanceStatus;
import com.mine.payment.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;

import static com.mine.payment.service.AccountServiceImplTest.getAccount;
import static org.junit.Assert.assertEquals;

/**
 * @stefanl
 */
@RunWith(SpringRunner.class)
public class TransferServiceImplTest {

    public static final String CURRENCY_ID = "EUR";

    @Autowired
    private TransferService transferService;

    @MockBean
    private TransferRepository transferRepository;

    @MockBean
    private AccountRepository accountRepository;

    @Before
    public void setUp() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Account debitAccount = newAccount(1, "Debit account", CURRENCY_ID, new BigDecimal(550.00),
                BalanceStatus.DR, now, now, AccountType.CLIENT);
        Account creditAccount = newAccount(2, "Credit account", CURRENCY_ID, new BigDecimal(90.00),
                BalanceStatus.CR, now, now, AccountType.CLIENT);
        Transfer newTransfer1 = createNewTransfer(debitAccount, creditAccount,
                new BigDecimal(50.00), now);
        Mockito.when(transferRepository.save(newTransfer1))
                .thenReturn(newTransfer1);

        Mockito.when(accountRepository.save(debitAccount))
                .thenReturn(debitAccount);

        Mockito.when(accountRepository.save(creditAccount))
                .thenReturn(creditAccount);

        Account debitedAccount = newAccount(1, "Debited account", CURRENCY_ID, new BigDecimal(500.00),
                BalanceStatus.DR, now, now, AccountType.CLIENT);

        Account creditedAccount = newAccount(2, "Credited account", CURRENCY_ID, new BigDecimal(40.00),
                BalanceStatus.CR, now, now, AccountType.CLIENT);

        Mockito.when(accountRepository.findById(debitedAccount.getId()))
                .thenReturn(Optional.of(debitedAccount));

        Mockito.when(accountRepository.findById(creditedAccount.getId()))
                .thenReturn(Optional.of(creditedAccount));
    }


    @Test
    public void saveTransfer() throws AccountsValidationException {
        TransferRequest transferRequest = createNewTransferRequest(1, 2, CURRENCY_ID, new BigDecimal(50.00));
        transferService.transferAmount(transferRequest);
    }

    @Test
    public void noDebitAccountFound() {
        TransferRequest request = createNewTransferRequest(100,4,CURRENCY_ID, new BigDecimal(10));
        try {
            transferService.transferAmount(request);
        } catch (AccountsValidationException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
        }
    }

    @Test
    public void noCreditAccountFound() {
        TransferRequest request = createNewTransferRequest(3,101, CURRENCY_ID, new BigDecimal(10));
        try {
            transferService.transferAmount(request);
        } catch (AccountsValidationException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getHttpStatus());
        }
    }

    @Test
    public void currencyMismatch() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Account debitAccount = newAccount(4, "Debit account", "USD", new BigDecimal(550.00),
                BalanceStatus.DR, now, now, AccountType.CLIENT);
        Mockito.when(accountRepository.findById(debitAccount.getId()))
                .thenReturn(Optional.of(debitAccount));


        TransferRequest request = createNewTransferRequest(1,4,CURRENCY_ID, new BigDecimal(10));
        try {
            transferService.transferAmount(request);
        } catch (AccountsValidationException e) {
            assertEquals(HttpStatus.PRECONDITION_FAILED, e.getHttpStatus());
        }
    }

    private TransferRequest createNewTransferRequest(long debitAccountId, long creditAccountId, String currencyId, BigDecimal amount) {
        TransferRequest newTransfer = new TransferRequest();
        newTransfer.setAmount(amount);
        newTransfer.setCurrencyId(currencyId);
        newTransfer.setCreditAccountId(debitAccountId);
        newTransfer.setDebitAccountId(creditAccountId);
        return newTransfer;
    }

    private Transfer createNewTransfer(Account debitAccount, Account creditAccount, BigDecimal amount, Timestamp now) {
        Transfer newTransfer = new Transfer();
        newTransfer.setCurrencyId(debitAccount.getCurrencyId());
        newTransfer.setExecutionDate(now);
        newTransfer.setCreditAccountId(creditAccount.getId());
        newTransfer.setDebitAccountId(debitAccount.getId());
        newTransfer.setAmount(amount);
        return newTransfer;
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

    @TestConfiguration
    static class TransferServiceImplTestContextConfiguration {

        @Bean
        public TransferService transferService() {
            return new TransferServiceImpl();
        }

        @Bean
        public AccountService accountService() {
            return new AccountServiceImpl();
        }

        @Bean
        public JsonUtil jsonUtil() {
            return new JsonUtil();
        }
    }
}