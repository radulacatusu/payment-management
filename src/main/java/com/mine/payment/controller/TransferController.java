package com.mine.payment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mine.payment.api.TransferRequest;
import com.mine.payment.model.Account;
import com.mine.payment.service.AccountService;
import com.mine.payment.service.TransferService;
import com.mine.payment.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @stefanl
 */
@RestController
@RequestMapping("/transfers")
public class TransferController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransferService transferService;

    @Autowired
    private JsonUtil jsonUtil;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity transfer(@Valid @RequestBody TransferRequest request) throws JsonProcessingException {

        Optional<Account> debitAccount = accountService.findById(request.getDebitAccountId());
        if (!debitAccount.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Optional<Account> creditAccount = accountService.findById(request.getCreditAccountId());
        if (!creditAccount.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        if (!creditAccount.get().getCurrencyId().equals(request.getCurrencyId())
                || !debitAccount.get().getCurrencyId().equals(request.getCurrencyId())) {
            Map<String, String> map = new HashMap<>();
            map.put("message", "Currency mismatch between this account and ledger account.");
            return new ResponseEntity(jsonUtil.writeValueAsString(map), null, HttpStatus.PRECONDITION_FAILED);
        }

        accountService.updateAccounts(debitAccount.get(), creditAccount.get(), request.getAmount());
        transferService.saveTransfer(debitAccount.get(), creditAccount.get(), request.getAmount());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
