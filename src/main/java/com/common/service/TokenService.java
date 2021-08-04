package com.common.service;

import com.common.dto.TokenDto;
import com.common.entity.Token;

public interface TokenService extends BaseService<Token, TokenDto>{
    void deleteAllByExpiryLessThan(long expiryDate);

}
