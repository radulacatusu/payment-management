package com.mine.payment.controller;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.mine.payment.PaymentApplication;
import com.mine.payment.api.LoadAccountRequest;
import com.mine.payment.api.TransferRequest;
import com.mine.payment.model.Account;
import com.mine.payment.security.JwtTokenProvider;
import com.mine.payment.util.AccountType;
import com.mine.payment.util.BalanceStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @stefanl
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PaymentApplication.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Transactional
public class AccountControllerTest {

    public static final String HEADER_TOKEN = "x-access-token";
    public static final String CURRENCY_ID = "EUR";

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private String token;

    @Test
    public void createAccount() {

        Account accountRequest = newAccount("Test Account", CURRENCY_ID);
        Response response = createAccountRequest(accountRequest);

        assertEquals(201, response.statusCode());
        String accountId = response.body().jsonPath().get("id");

        Account responseAccount = getAccount(Integer.valueOf(accountId)).body().as(Account.class);

        assertEquals(Long.valueOf(accountId), (Long) responseAccount.getId());
        assertEquals("Test Account", responseAccount.getName());
        assertEquals(CURRENCY_ID, responseAccount.getCurrencyId());
        assertTrue(responseAccount.getBalance().compareTo(new BigDecimal(0)) == 0);
        assertEquals(BalanceStatus.DR, responseAccount.getBalanceStatus());
        assertEquals(AccountType.CLIENT, responseAccount.getAccountType());
    }

    @Test
    public void createExistingAccount() {
        Account accountRequest = newAccount("Credit Account", CURRENCY_ID);
        Response response = createAccountRequest(accountRequest);
        assertEquals(409, response.statusCode());
    }

    @Test
    public void notExistingAccount() {
        Response response = getAccount(1001);
        assertEquals(404, response.statusCode());
    }

    @Test
    public void getLedgerAccount() {
        Account responseAccount = getAccount(Integer.valueOf(1)).body().as(Account.class);

        assertEquals(Long.valueOf(1), (Long) responseAccount.getId());
        assertEquals("Ledger", responseAccount.getName());
        assertEquals(CURRENCY_ID, responseAccount.getCurrencyId());
        assertEquals(BalanceStatus.DR, responseAccount.getBalanceStatus());
        assertEquals(AccountType.LEDGER, responseAccount.getAccountType());
    }

    @Test
    public void loadAccount() {
        LoadAccountRequest request = newLoadAccountRequest(new BigDecimal(500), CURRENCY_ID);
        Response response = loadAccountRequest(request, 2);
        assertEquals(200, response.statusCode());

        Account account = getAccount(2).body().as(Account.class);
        Account ledgerAccount = getAccount(1).body().as(Account.class);

        assertTrue(account.getBalance().compareTo(new BigDecimal(500)) == 0);
        assertEquals(BalanceStatus.DR, account.getBalanceStatus());

        assertTrue(ledgerAccount.getBalance().compareTo(new BigDecimal(999500)) == 0);
        assertEquals(BalanceStatus.DR, ledgerAccount.getBalanceStatus());
    }

    private Response getAccount(int accountId) {
        return given()
                .contentType(ContentType.JSON)
                .header(HEADER_TOKEN, token)
                .when()
                .get("/accounts/" + accountId);
    }

    private Response createAccountRequest(Account accountRequest) {
        return given()
                .contentType(ContentType.JSON)
                .header(HEADER_TOKEN, token)
                .body(accountRequest)
                .when()
                .post("/accounts");
    }

    private Response loadAccountRequest(LoadAccountRequest request,
                                        int accountId) {
        return given()
                .contentType(ContentType.JSON)
                .header(HEADER_TOKEN, token)
                .body(request)
                .when()
                .post("/accounts/" + accountId + "/load");
    }

    private Account newAccount(String nameOnAccount,
                               String currencyId) {
        Account account = new Account();
        account.setName(nameOnAccount);
        account.setCurrencyId(currencyId);
        return account;
    }

    public static LoadAccountRequest newLoadAccountRequest(BigDecimal amount,
                                                           String currencyId) {
        LoadAccountRequest account = new LoadAccountRequest();
        account.setAmount(amount);
        account.setCurrencyId(currencyId);
        return account;
    }

    @Before
    public void init() {
        token = jwtTokenProvider.createToken("Test");
    }
}
