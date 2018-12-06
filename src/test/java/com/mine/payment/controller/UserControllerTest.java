package com.mine.payment.controller;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.mine.payment.PaymentApplication;
import com.mine.payment.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

/**
 * @stefanl
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PaymentApplication.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Transactional
public class UserControllerTest {


    @Test
    public void signUp() {

        User request = newUser("Test Account", "Password");
        Response response = createUserRequest(request);

        assertEquals(201, response.statusCode());
    }

    @Test
    public void signUpExistingUser() {

        User request = newUser("Test Account 2", "Password");
        Response response = createUserRequest(request);

        assertEquals(201, response.statusCode());

        response = createUserRequest(request);

        assertEquals(409, response.statusCode());
    }

    private Response createUserRequest(User userRequest) {
        return given()
                .contentType(ContentType.JSON)
                .body(userRequest)
                .when()
                .post("/sign-up");
    }

    private User newUser(String name,
                         String password) {
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        return user;
    }
}
