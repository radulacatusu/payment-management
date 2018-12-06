package com.mine.payment.service;

import com.mine.payment.model.Account;
import com.mine.payment.model.Transfer;
import com.mine.payment.repository.TransferRepository;
import com.mine.payment.util.AccountType;
import com.mine.payment.util.BalanceStatus;
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

import static com.mine.payment.service.AccountServiceImplTest.getAccount;

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

    @Before
    public void setUp() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Account debitAccount = newAccount(1, "Debit account", CURRENCY_ID, new BigDecimal(500.00),
                BalanceStatus.DR, now, now, AccountType.CLIENT);
        Account creditAccount = newAccount(2, "Credit account", CURRENCY_ID, new BigDecimal(40.00),
                BalanceStatus.CR, now, now, AccountType.CLIENT);
        Transfer newTransfer1 = createNewTransfer(debitAccount, creditAccount,
                new BigDecimal(50.00), now);
        Transfer newTransfer2= createNewTransfer(creditAccount,debitAccount,
                new BigDecimal(50.00), now);
        Mockito.when(transferRepository.save(newTransfer1))
                .thenReturn(newTransfer1);
        Mockito.when(transferRepository.save(newTransfer2))
                .thenReturn(newTransfer2);
    }


    @Test
    public void saveTransfer() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Account debitedAccount = newAccount(1, "Debited account", CURRENCY_ID, new BigDecimal(500.00),
                BalanceStatus.DR, now, now, AccountType.CLIENT);

        Account creditedAccount = newAccount(2, "Credited account", CURRENCY_ID, new BigDecimal(40.00),
                BalanceStatus.CR, now, now, AccountType.CLIENT);
        transferService.saveTransfer(debitedAccount, creditedAccount, new BigDecimal(50.00));
    }

    private Transfer createNewTransfer(Account debitAccount, Account creditAccount, BigDecimal amount, Timestamp now) {
        Transfer newTransfer = new Transfer();
        newTransfer.setAmount(amount);
        newTransfer.setCurrencyId(debitAccount.getCurrencyId());
        newTransfer.setExecutionDate(now);
        newTransfer.setCreditAccountId(creditAccount.getId());
        newTransfer.setDebitAccountId(debitAccount.getId());
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
    }
}