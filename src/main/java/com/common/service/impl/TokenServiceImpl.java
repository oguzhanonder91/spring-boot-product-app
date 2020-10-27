package com.common.service.impl;

import com.common.entity.Token;
import com.common.repository.TokenRepository;
import com.common.service.TokenService;
import com.util.enums.TokenType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TokenServiceImpl extends BaseServiceImpl<Token> implements TokenService {
    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public List<Token> findByValueAndEmailAndDomainAndTokenTypeOrderByCreatedDateDesc(String token, String email, String domain, TokenType tokenType) {
        return tokenRepository.findByValueAndEmailAndDomainAndTokenTypeOrderByCreatedDateDesc(token, email, domain, tokenType);
    }

    @Override
    public Optional<Token> findByValueAndTokenTypeAndDomain(String token, TokenType tokenType, String domain) {
        return tokenRepository.findByValueAndTokenTypeAndDomain(token, tokenType, domain);
    }

    @Override
    public void deleteAllByExpiryLessThan(long expiryDate) {
        tokenRepository.deleteAllByExpiryLessThan(expiryDate);
    }
}
