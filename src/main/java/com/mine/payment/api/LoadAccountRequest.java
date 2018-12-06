package com.mine.payment.api;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mine.payment.annotation.CurrencyConstraint;
import com.mine.payment.util.BigDecimalSerializer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @stefanl
 */
public class LoadAccountRequest {

    @NotNull
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal amount;

    @NotBlank
    @CurrencyConstraint
    private String currencyId;

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "LoadAccountRequest{" +
                "amount=" + amount +
                ", currencyId='" + currencyId + '\'' +
                '}';
    }
}
