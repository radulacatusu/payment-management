package com.mine.payment.controller;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.ResponseOptions;
import com.mine.payment.PaymentApplication;
import com.mine.payment.api.TransferRequest;
import com.mine.payment.model.Account;
import com.mine.payment.security.JwtTokenProvider;
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
public class TransferControllerTest {

    public static final String HEADER_TOKEN = "x-access-token";
    public static final String CURRENCY_ID = "EUR";

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private String token;

    @Test
    public void noDebitAccountFound() {
        TransferRequest request = newTransferRequest(100,4,new BigDecimal(10), CURRENCY_ID);
        assertEquals(404,transferAmount(request).statusCode());
    }

    @Test
    public void noCreditAccountFound() {
        TransferRequest request = newTransferRequest(3,101,new BigDecimal(10), CURRENCY_ID);
        assertEquals(404,transferAmount(request).statusCode());
    }

    @Test
    public void currencyMismatch() {
        TransferRequest request = newTransferRequest(3,4,new BigDecimal(10), "USD");
        assertEquals(412,transferAmount(request).statusCode());
    }

    @Test
    public void transferTest() {

        TransferRequest request = newTransferRequest(3,4,new BigDecimal(10), CURRENCY_ID);
        assertEquals(200,transferAmount(request).statusCode());

        Account debitAccount = getAccount(3);
        Account creditAccount = getAccount(4);

        assertTrue(debitAccount.getBalance().compareTo(new BigDecimal(20)) == 0);
        assertEquals(BalanceStatus.CR, debitAccount.getBalanceStatus());

        assertTrue( creditAccount.getBalance().compareTo(new BigDecimal(20)) == 0);
        assertEquals( BalanceStatus.DR, creditAccount.getBalanceStatus());
    }

    private Account getAccount(int accountId) {
        return given()
                        .contentType(ContentType.JSON)
                        .header(HEADER_TOKEN, token)
                        .when()
                        .get("/accounts/" + accountId).body().as(Account.class);
    }

    private ResponseOptions transferAmount(TransferRequest transferRequest) {
        return given()
                .contentType(ContentType.JSON)
                .header(HEADER_TOKEN, token)
                .body(transferRequest)
                .when()
                .post("/transfers");
    }

    private TransferRequest newTransferRequest(int debitAccountId,
                                               int creditAccountId,
                                               BigDecimal amount,
                                               String currencyId) {
        TransferRequest tr = new TransferRequest();
        tr.setDebitAccountId((long) debitAccountId);
        tr.setCreditAccountId((long) creditAccountId);
        tr.setAmount(amount);
        tr.setCurrencyId(currencyId);
        return tr;
    }

    @Before
    public void init() {
        token = jwtTokenProvider.createToken("Test");
    }
}
