package com.common.dao;

import com.common.entity.Token;
import com.common.exception.BaseException;
import com.common.service.TokenService;
import com.util.enums.TokenType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class TokenDao {

    @Autowired
    private TokenService tokenService;

    public Token tokenPrepare(final Token token , final HttpServletRequest request) {
        if (StringUtils.isEmpty(token.getValue())) {
            throw new BaseException("Token üretilemedi");
        }
        token.setDomain(StringUtils.isEmpty(request.getHeader("origin")) ? "thirdPart" : request.getHeader("origin"));
        return token;
    }

    public Token create (Token token){
        return tokenService.save(token);
    }

    public Token controlToken(final String tokenParam, final String email, TokenType tokenType , final HttpServletRequest request) {
         String origin = "";
        if (!StringUtils.isEmpty(request.getHeader("origin"))) {
            origin = request.getHeader("origin");
        }
        final List<Token> tokens = tokenService.findByValueAndEmailAndDomainAndTokenTypeOrderByCreatedDateDesc(tokenParam, email, origin, tokenType);
        return tokens.size()> 0 ? tokens.get(0) : null;
    }

    public void deleteRealToken(Token validToken){
        if (validToken != null) {
            tokenService.deleteReal(validToken);
        }
    }
}
