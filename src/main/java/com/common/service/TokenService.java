package com.common.service;

import com.common.entity.Token;
import com.util.enums.TokenType;

import java.util.List;
import java.util.Optional;

public interface TokenService extends BaseService<Token>{
    List<Token> findByValueAndEmailAndDomainAndTokenTypeOrderByCreatedDateDesc(String token, String email, String domain, TokenType tokenType);
    Optional<Token> findByValueAndTokenTypeAndDomain(String token, TokenType tokenType, String domain);
}
