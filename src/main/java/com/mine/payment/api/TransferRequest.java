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
public class TransferRequest {

    @NotNull
    private Long debitAccountId;
    @NotNull
    private Long creditAccountId;

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

    public Long getDebitAccountId() {
        return debitAccountId;
    }

    public void setDebitAccountId(Long debitAccountId) {
        this.debitAccountId = debitAccountId;
    }

    public Long getCreditAccountId() {
        return creditAccountId;
    }

    public void setCreditAccountId(Long creditAccountId) {
        this.creditAccountId = creditAccountId;
    }

    @Override
    public String toString() {
        return "TransferRequest{" +
                "debitAccountId=" + debitAccountId +
                ", creditAccountId=" + creditAccountId +
                ", amount=" + amount +
                ", currencyId='" + currencyId + '\'' +
                '}';
    }
}
