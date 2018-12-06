package com.mine.payment.api;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mine.payment.util.BalanceStatus;
import com.mine.payment.util.BigDecimalSerializer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @stefanl
 */
public class LoadAccountResponse {

    private long accountId;

    @NotNull
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal balance;

    @NotBlank
    private BalanceStatus balanceStatus;

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BalanceStatus getBalanceStatus() {
        return balanceStatus;
    }

    public void setBalanceStatus(BalanceStatus balanceStatus) {
        this.balanceStatus = balanceStatus;
    }

    @Override
    public String toString() {
        return "LoadAccountResponse{" +
                "accountId=" + accountId +
                ", balance=" + balance +
                ", balanceStatus='" + balanceStatus + '\'' +
                '}';
    }
}
