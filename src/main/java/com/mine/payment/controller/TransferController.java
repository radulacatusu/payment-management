package com.mine.payment.controller;

import com.mine.payment.api.TransferRequest;
import com.mine.payment.exception.AccountsValidationException;
import com.mine.payment.service.TransferService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @stefanl
 */
@RestController
@RequestMapping("/transfers")
public class TransferController {

    @Autowired
    private TransferService transferService;


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity transfer(@Valid @RequestBody TransferRequest request) {

        try {
            transferService.transferAmount(request);
        } catch (AccountsValidationException e) {
            new ResponseEntity<>(StringUtils.isEmpty(e.getMessage()) ? null : e.getMessage(), null, e.getHttpStatus());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
