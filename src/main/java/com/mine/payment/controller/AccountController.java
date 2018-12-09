package com.mine.payment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mine.payment.api.LoadAccountRequest;
import com.mine.payment.api.LoadAccountResponse;
import com.mine.payment.exception.AccountsValidationException;
import com.mine.payment.model.Account;
import com.mine.payment.service.AccountService;
import com.mine.payment.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @stefanl
 */
@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private JsonUtil jsonUtil;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createAccount(@Valid @RequestBody Account account) throws JsonProcessingException {

        long id = accountService.createAccount(account);
        if (id == -1) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(id));
        return new ResponseEntity<>(jsonUtil.writeValueAsString(map), null, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable long id) {
        Optional<Account> account = accountService.findById(id);
        if (!account.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(account.get(), null, HttpStatus.OK);
    }

    @PostMapping(value = "{accountId}/load", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoadAccountResponse> loadAccount(@PathVariable long accountId,
                                                           @Valid @RequestBody LoadAccountRequest request) {

        LoadAccountResponse response = null;
        try {
            response = accountService.loadAccount(accountId, request);
        } catch (AccountsValidationException e) {
            new ResponseEntity<>(StringUtils.isEmpty(e.getMessage()) ? null : e.getMessage(), null, e.getHttpStatus());
        }

        return new ResponseEntity<>(response, null, HttpStatus.OK);
    }

}
