package com.common.service.impl;

import com.common.dto.TokenDto;
import com.common.entity.Token;
import com.common.repository.TokenRepository;
import com.common.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl extends BaseServiceImpl<Token, TokenDto> implements TokenService {
    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public void deleteAllByExpiryLessThan(long expiryDate) {
        tokenRepository.deleteAllByExpiryLessThan(expiryDate);
    }
}
