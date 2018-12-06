package com.mine.payment.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mine.payment.annotation.CurrencyConstraint;
import com.mine.payment.util.BigDecimalSerializer;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @stefanl
 */
@Entity
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @JsonSerialize(using = BigDecimalSerializer.class)
    @Column(scale = 2, precision = 15)
    private BigDecimal amount;

    @NotBlank
    @Column(name = "CURRENCY_ID", length = 3)
    @CurrencyConstraint
    private String currencyId;

    @NotNull
    @Column(name = "DEBIT_ACCOUNT_ID", unique = true)
    private long debitAccountId;

    @NotNull
    @Column(name = "CREDIT_ACCOUNT_ID", unique = true)
    private long creditAccountId;

    @NotNull
    @Column(name = "EXECUTION_DATE")
    private Timestamp executionDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public long getDebitAccountId() {
        return debitAccountId;
    }

    public void setDebitAccountId(long debitAccountId) {
        this.debitAccountId = debitAccountId;
    }

    public long getCreditAccountId() {
        return creditAccountId;
    }

    public void setCreditAccountId(long creditAccountId) {
        this.creditAccountId = creditAccountId;
    }

    public Timestamp getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Timestamp executionDate) {
        this.executionDate = executionDate;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "id=" + id +
                ", amount=" + amount +
                ", currencyId='" + currencyId + '\'' +
                ", debitAccountId=" + debitAccountId +
                ", creditAccountId=" + creditAccountId +
                ", executionDate=" + executionDate +
                '}';
    }
}
