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

    public Token tokenPrepare(final String paramToken, final String email, final HttpServletRequest request) {
        final Token token = new Token();
        token.setEmail(email);
        if (StringUtils.isEmpty(paramToken)) {
            throw new BaseException("Token üretilemedi");
        }
        token.setValue(paramToken);
        token.setTokenType(TokenType.AUTH);
        token.setDomain(StringUtils.isEmpty(request.getHeader("origin")) ? "thirdPart" : request.getHeader("origin"));
        return token;
    }

    public Token create (Token token){
        return tokenService.save(token);
    }

    public Token controlToken(final String tokenParam, final String email, final HttpServletRequest request) {
         String origin = "";
        if (!StringUtils.isEmpty(request.getHeader("origin"))) {
            origin = request.getHeader("origin");
        }
        final List<Token> tokens = tokenService.findByValueAndEmailAndDomainAndTokenTypeOrderByCreatedDateDesc(tokenParam, email, origin, TokenType.AUTH);
        return tokens.size()> 0 ? tokens.get(0) : null;
    }

    public void deleteRealToken(Token validToken){
        if (validToken != null) {
            tokenService.deleteReal(validToken);
        }
    }
}
