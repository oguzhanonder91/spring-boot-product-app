package com.common.service;

import com.common.entity.Token;
import com.util.enums.TokenType;

import java.util.List;

public interface TokenService extends BaseService<Token>{
    List<Token> findByValueAndEmailAndDomainAndTokenTypeOrderByCreatedDateDesc(String token, String email, String domain, TokenType tokenType);
}
