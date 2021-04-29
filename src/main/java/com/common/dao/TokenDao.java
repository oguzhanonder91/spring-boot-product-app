package com.common.dao;

import com.common.configuration.CaboryaConfig;
import com.common.dto.BaseResponse;
import com.common.entity.Token;
import com.common.exception.BaseException;
import com.common.service.TokenService;
import com.common.specification.SearchCriteria;
import com.common.specification.SearchOperation;
import com.util.CommonUtil;
import com.util.enums.TokenType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        return tokenService.saveForEntity(token);
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
        return tokenPrepare(newTokenObj, httpServletRequest);
    }

    public Token controlToken(final String tokenParam, final String email, TokenType tokenType, final HttpServletRequest request) {
        String origin = "";
        if (!StringUtils.isEmpty(request.getHeader("origin"))) {
            origin = request.getHeader("origin");
        }
        final List<Token> tokens = this.findByValueAndEmailAndDomainAndTokenTypeOrderByCreatedDateDesc(tokenParam, email, origin, tokenType);
        return tokens.size() > 0 ? tokens.get(0) : null;
    }

    public Token controlTokenRegistrationAndPassword(final String tokenParam, final TokenType tokenType, final HttpServletRequest request) {
        String origin = "";
        if (!StringUtils.isEmpty(request.getHeader("origin"))) {
            origin = request.getHeader("origin");
        }
        final Optional<Token> token = this.findByValueAndTokenTypeAndDomain(tokenParam, tokenType, origin);
        return token.orElse(null);
    }

    public Optional<Token> findByValueAndTokenTypeAndDomain(String tokenParam, TokenType tokenType, String origin) {
        SearchCriteria searchCriteria = new SearchCriteria.Builder()
                .and(SearchOperation.EQUAL, "value", tokenParam)
                .and(SearchOperation.EQUAL, "tokenType", tokenType)
                .and(SearchOperation.EQUAL, "domain", origin)
                .build();
        List<Token> tokens = tokenService.caboryaFindByParamsForEntity(searchCriteria);
        return tokens.size() > 0 ? Optional.of(tokens.get(0)) : Optional.empty();
    }

    public List<Token> findByValueAndEmailAndDomainAndTokenTypeOrderByCreatedDateDesc(String tokenParam, String email, String origin , TokenType tokenType) {
        SearchCriteria searchCriteria = new SearchCriteria.Builder()
                .and(SearchOperation.EQUAL, "value", tokenParam)
                .and(SearchOperation.EQUAL, "tokenType", tokenType)
                .and(SearchOperation.EQUAL, "domain", origin)
                .and(SearchOperation.EQUAL, "email", email)
                .sort(Sort.Direction.DESC,"createdDate")
                .build();
        return tokenService.caboryaFindByParamsForEntity(searchCriteria);
    }

    public void deleteRealToken(Token validToken) {
        if (validToken != null) {
            tokenService.realDeleteForEntity(validToken);
        }
    }

    public void deleteTokensByExpiryLess(long expiry) {
        tokenService.deleteAllByExpiryLessThan(expiry);
    }

    public ResponseEntity<BaseResponse<String>> controlTokenReturnBaseResponse(Token token, HttpServletRequest httpServletRequest,
                                                                               MessageSource messageSource, String param,
                                                                               String invalidToken, String expired, String valid) {
        BaseResponse<String> baseResponse = new BaseResponse<>();
        baseResponse.setData(param);
        if (token == null) {
            baseResponse.setMessage(messageSource.getMessage(invalidToken, null, httpServletRequest.getLocale()));
            baseResponse.setKey(CommonUtil.TOKEN_INVALID);
            return new ResponseEntity<>(baseResponse, HttpStatus.OK);
        }
        Calendar cal = Calendar.getInstance();
        if ((token.getExpiry() - cal.getTimeInMillis()) <= 0) {
            this.deleteRealToken(token);
            baseResponse.setMessage(messageSource.getMessage(expired, null, httpServletRequest.getLocale()));
            baseResponse.setKey(CommonUtil.TOKEN_EXPIRED);
            return new ResponseEntity<>(baseResponse, HttpStatus.OK);
        }
        baseResponse.setMessage(messageSource.getMessage(valid, null, httpServletRequest.getLocale()));
        baseResponse.setKey(CommonUtil.TOKEN_VALID);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
}
