package com.mine.payment.service;

import com.mine.payment.model.Account;
import com.mine.payment.model.Transfer;
import com.mine.payment.repository.TransferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * @stefanl
 */
@Service
@Transactional
public class TransferServiceImpl implements TransferService {

    @Resource
    private TransferRepository transferRepository;

    @Override
    public void saveTransfer(Account debitAccount, Account creditAccount, BigDecimal amount) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Transfer newTransfer = createNewTransfer(debitAccount, creditAccount,
                amount, now);
        transferRepository.save(newTransfer);
        newTransfer = createNewTransfer(creditAccount, debitAccount,
                amount, now);
        transferRepository.save(newTransfer);
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
}
