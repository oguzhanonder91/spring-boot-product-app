package com.common.service;

import com.common.dto.TokenDto;
import com.common.entity.Token;
import com.util.enums.TokenType;

import java.util.List;
import java.util.Optional;

public interface TokenService extends BaseService<Token, TokenDto>{
    void deleteAllByExpiryLessThan(long expiryDate);

}
