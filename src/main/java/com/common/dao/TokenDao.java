package com.common.dao;

import com.common.configuration.CaboryaConfig;
import com.common.entity.Token;
import com.common.exception.BaseException;
import com.common.service.TokenService;
import com.util.EmailUtil;
import com.util.enums.TokenType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class TokenDao {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private CaboryaConfig caboryaConfig;

    public Token tokenPrepare(final Token token, final HttpServletRequest request) {
        if (StringUtils.isEmpty(token.getValue())) {
            throw new BaseException("Don't prepare token");
        }
        token.setDomain(StringUtils.isEmpty(request.getHeader("origin")) ? "thirdPart" : request.getHeader("origin"));
        return token;
    }

    public Token create(Token token) {
        return tokenService.save(token);
    }

    public Token prepareRegistrationAndPassword(String email, TokenType tokenType, HttpServletRequest httpServletRequest) {
        long issued = new Date().getTime();
        long expiration = caboryaConfig.getSecurity().getTokenRegistrationAndResetPasswordExpiration() * 1000;
        String token = UUID.randomUUID().toString();
        Token newTokenObj = new Token()
                .value(token)
                .email(email)
                .tokenType(tokenType)
                .expiry(issued + expiration)
                .issuedAt(issued);
        Token tokenObj = tokenPrepare(newTokenObj, httpServletRequest);
        return tokenObj;
    }

    public Token controlToken(final String tokenParam, final String email, TokenType tokenType, final HttpServletRequest request) {
        String origin = "";
        if (!StringUtils.isEmpty(request.getHeader("origin"))) {
            origin = request.getHeader("origin");
        }
        final List<Token> tokens = tokenService.findByValueAndEmailAndDomainAndTokenTypeOrderByCreatedDateDesc(tokenParam, email, origin, tokenType);
        return tokens.size() > 0 ? tokens.get(0) : null;
    }

    public Token controlTokenRegistrationAndPassword(final String tokenParam, final TokenType tokenType, final HttpServletRequest request) {
        String origin = "";
        if (!StringUtils.isEmpty(request.getHeader("origin"))) {
            origin = request.getHeader("origin");
        }
        final Optional<Token> token = tokenService.findByValueAndTokenTypeAndDomain(tokenParam, tokenType, origin);
        return (Token) token.orElse(null);
    }

    public void deleteRealToken(Token validToken) {
        if (validToken != null) {
            tokenService.deleteReal(validToken);
        }
    }

    public void deleteTokensByExpiryLess(long expiry) {
        tokenService.deleteAllByExpiryLessThan(expiry);
    }
}
