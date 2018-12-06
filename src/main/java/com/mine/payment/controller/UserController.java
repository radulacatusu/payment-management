package com.mine.payment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mine.payment.model.User;
import com.mine.payment.security.JwtTokenProvider;
import com.mine.payment.service.UserService;
import com.mine.payment.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @stefanl
 */
@RestController
public class UserController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private JsonUtil jsonUtil;

    @PostMapping(value = "/sign-up", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> signUp(@Valid @RequestBody User user) throws JsonProcessingException {
        boolean rNew = userService.createUser(user);
        if (!rNew) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        String token = jwtTokenProvider.createToken(user.getName());

        Map<String, String> response = new HashMap<>();
        response.put("access_token", token);
        return new ResponseEntity<>(jsonUtil.writeValueAsString(response), null, HttpStatus.CREATED);
    }
}
