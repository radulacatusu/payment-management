package com.mine.payment.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @stefanl
 */
@Component
public class JwtConfig {

    @Value("${security.jwt.accessTokens:/sign-up}")
    private String signUpUrl;

    @Value("${security.jwt.accessTokens:/access-tokens}")
    private String accessTokens;

    @Value("${security.jwt.header:X-access-token}")
    private String header;

    @Value("${security.jwt.prefix:Bearer }")
    private String prefix;

    @Value("${security.jwt.expiration:6000000}")
    private int expiration;

    @Value("${security.jwt.secret:6LmexwZ723}")
    private String secret;

    public String getHeader() {
        return header;
    }

    public int getExpiration() {
        return expiration;
    }

    public String getSecret() {
        return secret;
    }

    public String getAccessTokens() {
        return accessTokens;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSignUpUrl() {
        return signUpUrl;
    }
}
