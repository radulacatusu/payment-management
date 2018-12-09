package com.mine.payment.service;

import com.mine.payment.api.TransferRequest;
import com.mine.payment.exception.AccountsValidationException;
import com.mine.payment.model.Account;
import com.mine.payment.model.Transfer;
import com.mine.payment.repository.TransferRepository;
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
import java.util.Optional;


/**
 * @stefanl
 */
@Service
@Transactional(isolation = Isolation.SERIALIZABLE)
public class TransferServiceImpl implements TransferService {

    @Resource
    private TransferRepository transferRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private JsonUtil jsonUtil;

    @Override
    public void transferAmount(TransferRequest request) throws AccountsValidationException {
        Optional<Account> debitAccount = accountService.findById(request.getDebitAccountId());
        if (!debitAccount.isPresent()) {
            throw new AccountsValidationException(HttpStatus.NOT_FOUND);
        }

        Optional<Account> creditAccount = accountService.findById(request.getCreditAccountId());
        if (!creditAccount.isPresent()) {
            throw new AccountsValidationException(HttpStatus.NOT_FOUND);
        }

        if (!creditAccount.get().getCurrencyId().equals(request.getCurrencyId())
                || !debitAccount.get().getCurrencyId().equals(request.getCurrencyId())) {
            Map<String, String> map = new HashMap<>();
            map.put("message", "Currency mismatch between this account and ledger account.");
            throw new AccountsValidationException(jsonUtil.writeValueAsString(map), HttpStatus.PRECONDITION_FAILED);
        }

        Timestamp now = new Timestamp(System.currentTimeMillis());
        accountService.updateAccounts(debitAccount.get(), creditAccount.get(), request.getAmount());
        Transfer newTransfer = createNewTransfer(debitAccount.get(), creditAccount.get(),
                request.getAmount(), now);
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
