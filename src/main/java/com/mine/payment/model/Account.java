package com.mine.payment.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mine.payment.annotation.CurrencyConstraint;
import com.mine.payment.util.BigDecimalSerializer;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @stefanl
 */
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Column(name = "NAME", unique = true)
    @JsonProperty("nameOnAccount")
    private String name;

    @NotBlank
    @Column(name = "CURRENCY_ID", length = 3)
    @CurrencyConstraint
    private String currencyId;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @Column(scale = 2, precision = 15)
    private BigDecimal balance;

    @Column(name = "BALANCE_STATUS", length = 2)
    private String balanceStatus;

    @Column(name = "BALANCE_TIMESTAMP")
    private Timestamp balanceTimestamp;

    @Column(name = "DATE_OPENED")
    private Timestamp dateOpened;

    @Column(name = "ACCOUNT_TYPE", length = 6)
    private String accountType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getBalanceStatus() {
        return balanceStatus;
    }

    public void setBalanceStatus(String balanceStatus) {
        this.balanceStatus = balanceStatus;
    }

    public Timestamp getBalanceTimestamp() {
        return balanceTimestamp;
    }

    public void setBalanceTimestamp(Timestamp balanceTimestamp) {
        this.balanceTimestamp = balanceTimestamp;
    }

    public Timestamp getDateOpened() {
        return dateOpened;
    }

    public void setDateOpened(Timestamp dateOpened) {
        this.dateOpened = dateOpened;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", currencyId='" + currencyId + '\'' +
                ", balance=" + balance +
                ", balanceStatus='" + balanceStatus + '\'' +
                ", balanceTimestamp=" + balanceTimestamp +
                ", dateOpened=" + dateOpened +
                ", accountType='" + accountType + '\'' +
                '}';
    }
}
