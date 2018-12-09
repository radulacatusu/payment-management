package com.mine.payment.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mine.payment.annotation.CurrencyConstraint;
import com.mine.payment.util.AccountType;
import com.mine.payment.util.BalanceStatus;
import com.mine.payment.util.BigDecimalSerializer;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "BALANCE_STATUS", length = 2)
    private BalanceStatus balanceStatus;

    @Column(name = "BALANCE_TIMESTAMP")
    private Timestamp balanceTimestamp;

    @Column(name = "DATE_OPENED")
    private Timestamp dateOpened;

    @Enumerated(EnumType.STRING)
    @Column(name = "ACCOUNT_TYPE", length = 6)
    private AccountType accountType;

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

    public BalanceStatus getBalanceStatus() {
        return balanceStatus;
    }

    public void setBalanceStatus(BalanceStatus balanceStatus) {
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

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id &&
                Objects.equals(name, account.name) &&
                Objects.equals(currencyId, account.currencyId) &&
                accountType == account.accountType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, currencyId, accountType);
    }
}
