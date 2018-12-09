package com.mine.payment.exception;

import org.springframework.http.HttpStatus;

/**
 * @stefanl
 */
public class AccountsValidationException extends Exception {
    private HttpStatus httpStatus;

    public AccountsValidationException(String errorMessage) {
        super(errorMessage);
    }

    public AccountsValidationException(String errorMessage, HttpStatus httpStatus) {
        super(errorMessage);
        this.httpStatus = httpStatus;

    }

    public AccountsValidationException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
